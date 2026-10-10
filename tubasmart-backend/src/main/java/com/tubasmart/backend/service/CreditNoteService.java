package com.tubasmart.backend.service;

import com.tubasmart.backend.model.*;
import com.tubasmart.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*; import java.time.*;

@Service
public class CreditNoteService {
    private final CreditNoteRepository notes; private final InvoiceRepository invoices; private final DocumentCounterRepository counters;
    public CreditNoteService(CreditNoteRepository n,InvoiceRepository i,DocumentCounterRepository c){notes=n;invoices=i;counters=c;}
    public record CreateRequest(Long invoiceId,BigDecimal taxableValue,String reason){}
    @Transactional public CreditNote issue(CreateRequest r){
        if(r.invoiceId()==null) throw new IllegalArgumentException("invoiceId is required");
        Invoice inv=invoices.findById(r.invoiceId()).orElseThrow(() -> new java.util.NoSuchElementException("Invoice not found"));
        if(inv.getStatus()!=DocumentStatus.ISSUED) throw new IllegalStateException("Credit note can only be issued against an issued invoice");
        BigDecimal value=nvl(r.taxableValue()); if(value.signum()<=0) throw new IllegalArgumentException("Credit note value must be greater than zero");
        BigDecimal already=notes.findByOriginalInvoiceId(inv.getId()).stream().filter(x->x.getStatus()!=DocumentStatus.CANCELLED).map(CreditNote::getTaxableValue).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal remaining=inv.getSubtotal().subtract(inv.getDiscount()).subtract(already);
        if(value.compareTo(remaining)>0) throw new IllegalStateException("Credit note exceeds the remaining invoice value");
        BigDecimal taxBase=inv.getSubtotal().subtract(inv.getDiscount()); BigDecimal originalTax=inv.getCgst().add(inv.getSgst()).add(inv.getIgst()).add(inv.getCess());
        BigDecimal tax=taxBase.signum()==0?BigDecimal.ZERO:value.multiply(originalTax).divide(taxBase,2,RoundingMode.HALF_UP);
        CreditNote n=new CreditNote();n.setOriginalInvoice(inv);n.setTaxableValue(money(value));n.setTaxImpact(money(tax));n.setTotal(money(value.add(tax)));n.setReason(r.reason());n.setStatus(DocumentStatus.ISSUED);n.setDocumentNumber(next(inv.getBillingPeriod()));return notes.save(n);
    }
    private String next(YearMonth p){int y=p.getMonthValue()>=4?p.getYear():p.getYear()-1;String fy=y+"-"+String.format("%02d",(y+1)%100);var c=counters.lockByTypeAndYear("CRN",fy).orElseGet(()->{var x=new DocumentCounter();x.setDocumentType("CRN");x.setFinancialYear(fy);return x;});c.setLastNumber(c.getLastNumber()+1);counters.save(c);return "CRN/"+fy+"/"+String.format("%05d",c.getLastNumber());}
    private BigDecimal nvl(BigDecimal x){return x==null?BigDecimal.ZERO:x;} private BigDecimal money(BigDecimal x){return nvl(x).setScale(2,RoundingMode.HALF_UP);}
}
