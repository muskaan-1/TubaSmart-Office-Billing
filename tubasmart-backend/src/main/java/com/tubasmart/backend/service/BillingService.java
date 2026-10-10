package com.tubasmart.backend.service;

import com.tubasmart.backend.api.BadRequestException;
import com.tubasmart.backend.model.*;
import com.tubasmart.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.*;
import java.time.*;
import java.util.*;

@Service
public class BillingService {
    private final InvoiceRepository invoices;
    private final CustomerRepository customers;
    private final TenantAllotmentRepository allotments;
    private final TenantChargeScheduleRepository schedules;
    private final FloorPriceRepository floorPrices;
    private final ChargeTypeRepository chargeTypes;
    private final SacHsnGstRepository taxes;
    private final InvoiceLineRepository lines;
    private final DocumentCounterRepository counters;
    private final CompanyRepository companies;
    private final InvoiceTypeRepository invoiceTypes;

    public BillingService(InvoiceRepository invoices, CustomerRepository customers,
                          TenantAllotmentRepository allotments, TenantChargeScheduleRepository schedules,
                          FloorPriceRepository floorPrices, ChargeTypeRepository chargeTypes,
                          SacHsnGstRepository taxes, InvoiceLineRepository lines,
                          DocumentCounterRepository counters, CompanyRepository companies,
                          InvoiceTypeRepository invoiceTypes) {
        this.invoices=invoices; this.customers=customers; this.allotments=allotments;
        this.schedules=schedules; this.floorPrices=floorPrices; this.chargeTypes=chargeTypes;
        this.taxes=taxes; this.lines=lines; this.counters=counters; this.companies=companies;
        this.invoiceTypes=invoiceTypes;
    }

    public record PreviewLine(Long chargeTypeId, String chargeType, String description, String sacHsnCode,
                              BigDecimal quantity, BigDecimal rate, BigDecimal amount, BigDecimal discount,
                              BigDecimal taxable, BigDecimal taxRate, BigDecimal taxAmount) {}
    public record BillingPreview(Long tenantId, String billingPeriod, List<PreviewLine> lines,
                                 BigDecimal subtotal, BigDecimal discount, BigDecimal taxable,
                                 BigDecimal cgst, BigDecimal sgst, BigDecimal igst, BigDecimal total,
                                 BigDecimal tds, BigDecimal netPayable) {}

    public BillingPreview preview(Long tenantId, YearMonth period, String supplyState, String companyState) {
        Customer tenant = customers.findById(tenantId).orElseThrow(() -> new BadRequestException("TENANT_NOT_FOUND", "Tenant not found"));
        TenantAllotment a = activeAllotment(tenantId, period);
        Company company = companies.findAll().stream().findFirst().orElse(null);
        String companyStateCode = companyState == null || companyState.isBlank() ? (company == null ? null : company.getStateCode()) : companyState;
        String pos = supplyState == null || supplyState.isBlank() ? tenant.getState() : supplyState;

        List<Charge> charges = mergedCharges(a, period.atEndOfMonth());
        if (charges.isEmpty()) throw new BadRequestException("NO_BILLABLE_CHARGES", "No recurring charges are configured for this allotment");

        BigDecimal subtotal=zero();
        BigDecimal discountPct = nvl(a.getDiscountPct());
        List<PreviewLine> result=new ArrayList<>();
        BigDecimal cgst=zero(), sgst=zero(), igst=zero();
        for (Charge c : charges) {
            if (c.amount.signum() <= 0 || c.chargeType == null || !c.chargeType.isRecurring()) continue;
            Tax tax = taxFor(c.chargeType);
            BigDecimal lineDiscount = money(c.amount.multiply(discountPct).divide(hundred(), 4, RoundingMode.HALF_UP));
            BigDecimal taxable = money(c.amount.subtract(lineDiscount).max(zero()));
            BigDecimal taxAmount = money(taxable.multiply(tax.rate).divide(hundred(), 2, RoundingMode.HALF_UP));
            if (sameState(pos, companyStateCode)) {
                BigDecimal h=money(taxAmount.divide(two(),2,RoundingMode.HALF_UP)); cgst=cgst.add(h); sgst=sgst.add(taxAmount.subtract(h));
            } else igst=igst.add(taxAmount);
            String desc=(c.chargeType.getDefaultDescription()==null?c.chargeType.getName():c.chargeType.getDefaultDescription())
                    .replace("{period}", period.getMonth().name()+" "+period.getYear())
                    .replace("{floor}", a.getFloor().getFloorName())
                    .replace("{building}", a.getFloor().getBuilding().getName());
            result.add(new PreviewLine(c.chargeType.getId(), c.chargeType.getName(), desc, tax.code,
                    one(), money(c.amount), money(c.amount), lineDiscount, taxable, tax.rate, taxAmount));
            subtotal=subtotal.add(c.amount);
        }
        subtotal=money(subtotal); BigDecimal discount=money(result.stream().map(PreviewLine::discount).reduce(zero(),BigDecimal::add));
        BigDecimal taxable=money(subtotal.subtract(discount)); cgst=money(cgst); sgst=money(sgst); igst=money(igst);
        BigDecimal total=money(taxable.add(cgst).add(sgst).add(igst));
        BigDecimal tdsRate=nvl(tenant.getTdsRate()); BigDecimal tds=money(taxable.multiply(tdsRate).divide(hundred(),2,RoundingMode.HALF_UP));
        return new BillingPreview(tenantId,period.toString(),result,subtotal,discount,taxable,cgst,sgst,igst,total,tds,money(total.subtract(tds)));
    }

    @Transactional
    public Invoice generateFromPreview(Long tenantId, YearMonth period, String supplyState, String companyState) {
        if (invoices.existsByTenantIdAndBillingPeriodAndStatusNot(tenantId,period,DocumentStatus.CANCELLED))
            throw new BadRequestException("DUPLICATE_BILLING", "An active invoice already exists for this tenant and billing period");
        BillingPreview p=preview(tenantId,period,supplyState,companyState);
        Customer tenant=customers.findById(tenantId).orElseThrow();
        Company company=companies.findAll().stream().findFirst().orElse(null);
        Invoice i=new Invoice(); i.setTenant(tenant); i.setBillingPeriod(period); i.setSupplyState(supplyState==null||supplyState.isBlank()?tenant.getState():supplyState);
        TenantAllotment a=activeAllotment(tenantId,period); i.setAllotmentId(a.getId()); i.setFloorId(a.getFloor().getId()); i.setBuildingId(a.getFloor().getBuilding().getId());
        i.setPeriodFrom(period.atDay(1)); i.setPeriodTo(period.atEndOfMonth());
        i.setDueDate(dueDate(i.getPeriodFrom(), tenant, a)); i.setDocumentNumber(nextNumber("INV",period)); i.setStatus(DocumentStatus.ISSUED);
        i.setSubtotal(p.subtotal()); i.setDiscount(p.discount()); i.setCgst(p.cgst()); i.setSgst(p.sgst()); i.setIgst(p.igst()); i.setTotal(p.total()); i.setTds(p.tds()); i.setTdsRate(nvl(tenant.getTdsRate())); i.setNetPayable(p.netPayable());
        invoiceTypes.findAll().stream().filter(x->x.isActive() && "SALE".equalsIgnoreCase(x.getNature())).findFirst().ifPresent(x->i.setInvoiceTypeId(x.getId()));
        Invoice saved=invoices.save(i);
        for (PreviewLine x:p.lines()) {
            InvoiceLine l=new InvoiceLine(); l.setInvoice(saved); l.setChargeType(x.chargeType()); l.setChargeTypeId(x.chargeTypeId()); l.setDescription(x.description()); l.setSacHsnCode(x.sacHsnCode());
            l.setQuantity(x.quantity()); l.setRate(x.rate()); l.setAmount(x.amount()); l.setDiscount(x.discount()); l.setTaxable(x.taxable()); l.setTaxRate(x.taxRate()); l.setTaxAmount(x.taxAmount());
            splitTax(l, x.taxAmount(), i.getSupplyState(), company==null?companyState:(companyState==null||companyState.isBlank()?company.getStateCode():companyState));
            lines.save(l);
        }
        return saved;
    }

    @Transactional
    public Invoice generate(Long tenantId, YearMonth period, BigDecimal taxableAmount, BigDecimal gstRate,
                            String supplyState, String companyState) {
        // Kept for the existing Angular frontend. The amount/rate are accepted as an explicit one-line invoice.
        if (invoices.existsByTenantIdAndBillingPeriodAndStatusNot(tenantId, period, DocumentStatus.CANCELLED))
            throw new BadRequestException("DUPLICATE_BILLING", "An active invoice already exists for this tenant and billing period");
        Customer tenant=customers.findById(tenantId).orElseThrow(() -> new BadRequestException("TENANT_NOT_FOUND","Tenant not found"));
        Invoice i=new Invoice(); i.setTenant(tenant); i.setBillingPeriod(period); i.setSupplyState(supplyState==null?tenant.getState():supplyState);
        i.setPeriodFrom(period.atDay(1)); i.setPeriodTo(period.atEndOfMonth()); i.setDueDate(period.atEndOfMonth()); i.setDocumentNumber(nextNumber("INV",period)); i.setStatus(DocumentStatus.ISSUED);
        BigDecimal amount=money(nvl(taxableAmount)); BigDecimal tax=money(amount.multiply(nvl(gstRate)).divide(hundred(),2,RoundingMode.HALF_UP));
        if(sameState(i.getSupplyState(),companyState)){BigDecimal h=money(tax.divide(two(),2,RoundingMode.HALF_UP));i.setCgst(h);i.setSgst(tax.subtract(h));}else i.setIgst(tax);
        i.setSubtotal(amount);i.setDiscount(zero());i.setTotal(money(amount.add(tax)));i.setNetPayable(i.getTotal());
        Invoice saved=invoices.save(i); InvoiceLine line=new InvoiceLine(); line.setInvoice(saved); line.setChargeType("MONTHLY"); line.setDescription("Monthly billing - "+period);line.setQuantity(one());line.setRate(amount);line.setAmount(amount);line.setTaxable(amount);line.setTaxRate(nvl(gstRate));line.setTaxAmount(tax);lines.save(line);return saved;
    }

    private void splitTax(InvoiceLine l, BigDecimal tax, String pos, String companyStateCode) {
        BigDecimal t = nvl(tax);
        if (sameState(pos, companyStateCode)) {
            BigDecimal half = money(t.divide(two(), 2, RoundingMode.HALF_UP));
            l.setCgst(half); l.setSgst(t.subtract(half)); l.setIgst(zero());
        } else {
            l.setCgst(zero()); l.setSgst(zero()); l.setIgst(t);
        }
        l.setCess(zero());
    }

    private record Charge(ChargeType chargeType, BigDecimal amount) {}
    private record Tax(String code, BigDecimal rate) {}

    private List<Charge> mergedCharges(TenantAllotment a, LocalDate onDate) {
        Map<Long,Charge> out=new LinkedHashMap<>();
        floorPrices.findByFloorIdAndActiveTrue(a.getFloor().getId()).stream().filter(p->!p.getEffectiveDate().isAfter(onDate))
                .sorted(Comparator.comparing(FloorPrice::getEffectiveDate)).forEach(p->{ChargeType ct=chargeType(p.getChargeType()); if(ct!=null) out.put(ct.getId(),new Charge(ct,price(p,a)));});
        schedules.findByAllotmentIdAndActiveTrue(a.getId()).forEach(s->{ChargeType ct=chargeType(s.getChargeType()); if(ct==null) return; BigDecimal amount=s.getOverrideAmount()!=null?s.getOverrideAmount():s.getActualAmount(); if(amount==null){ if("PerSqFt".equalsIgnoreCase(s.getBasis())) amount=nvl(s.getRate()).multiply(nvl(a.getFloor().getChargeableArea())); else amount=nvl(s.getRate()); } if("Actual".equalsIgnoreCase(s.getBasis()) || !s.isAutoBill()) return; out.put(ct.getId(),new Charge(ct,money(amount)));});
        return out.values().stream().filter(c->c.chargeType.isRecurring()).toList();
    }
    private BigDecimal price(FloorPrice p,TenantAllotment a){ if(p.getFixedRate()!=null) return money(p.getFixedRate()); if(p.getPerSqFtRate()!=null) return money(p.getPerSqFtRate().multiply(nvl(a.getFloor().getChargeableArea()))); return zero(); }
    private ChargeType chargeType(String value){if(value==null)return null;return chargeTypes.findByNameIgnoreCaseOrCodeIgnoreCase(value,value).stream().findFirst().orElse(null);}
    private Tax taxFor(ChargeType ct){SacHsnGst t=ct.getSacHsnCode()==null?null:taxes.findByCodeIgnoreCaseAndActiveTrue(ct.getSacHsnCode()).stream().findFirst().orElse(null);return t==null?new Tax(ct.getSacHsnCode(),zero()):new Tax(t.getCode(),t.isApplicable()?nvl(t.getRate()):zero());}
    private TenantAllotment activeAllotment(Long tenantId,YearMonth p){return allotments.findByTenantIdAndActiveTrue(tenantId).stream().filter(a->(a.getOccupancyStart()==null||!a.getOccupancyStart().isAfter(p.atEndOfMonth()))&&(a.getOccupancyEnd()==null||!a.getOccupancyEnd().isBefore(p.atDay(1)))&&a.getFloor().isActive()).findFirst().orElseThrow(()->new BadRequestException("NO_ALLOTMENT","No active tenant allotment found for this billing period"));}
    private LocalDate dueDate(LocalDate invoiceDate,Customer tenant,TenantAllotment a){int day=Math.max(1,Math.min(a.getBillingDay(),invoiceDate.lengthOfMonth()));LocalDate base=invoiceDate.withDayOfMonth(day);return base.plusDays(Math.max(0,tenant.getCreditDays()));}
    private String nextNumber(String type,YearMonth p){int y=p.getMonthValue()>=4?p.getYear():p.getYear()-1;String fy=y+"-"+String.format("%02d",(y+1)%100);var c=counters.lockByTypeAndYear(type,fy).orElseGet(()->{var x=new DocumentCounter();x.setDocumentType(type);x.setFinancialYear(fy);return x;});c.setLastNumber(c.getLastNumber()+1);counters.save(c);String prefix=companies.findAll().stream().findFirst().map(Company::getInvPrefix).filter(x->x!=null&&!x.isBlank()).orElse(type);return prefix+"/"+fy+"/"+String.format("%04d",c.getLastNumber());}
    private boolean sameState(String a,String b){return a!=null&&b!=null&&!a.isBlank()&&a.equalsIgnoreCase(b);}
    private BigDecimal nvl(BigDecimal x){return x==null?zero():x;} private BigDecimal money(BigDecimal x){return nvl(x).setScale(2,RoundingMode.HALF_UP);} private BigDecimal zero(){return BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP);} private BigDecimal one(){return BigDecimal.ONE;} private BigDecimal two(){return BigDecimal.valueOf(2);} private BigDecimal hundred(){return BigDecimal.valueOf(100);}
}
