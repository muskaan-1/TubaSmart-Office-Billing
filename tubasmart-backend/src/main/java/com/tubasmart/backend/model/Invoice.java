package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="invoices", indexes={@Index(name="idx_invoice_period_tenant", columnList="billingPeriod,tenant_id")})
public class Invoice extends BaseEntity {
    @Column(nullable=false, unique=true) private String documentNumber;
    @ManyToOne(optional=false) private Customer tenant;
    @Column(nullable=false) private java.time.YearMonth billingPeriod;
    private java.time.LocalDate dueDate;
    private String supplyState;
    @Enumerated(EnumType.STRING) private DocumentStatus status = DocumentStatus.DRAFT;
    private java.math.BigDecimal subtotal = java.math.BigDecimal.ZERO, discount = java.math.BigDecimal.ZERO;
    private java.math.BigDecimal cgst = java.math.BigDecimal.ZERO, sgst = java.math.BigDecimal.ZERO, igst = java.math.BigDecimal.ZERO;
    private java.math.BigDecimal cess = java.math.BigDecimal.ZERO, tds = java.math.BigDecimal.ZERO, total = java.math.BigDecimal.ZERO;
    private java.math.BigDecimal tdsRate = java.math.BigDecimal.ZERO, netPayable = java.math.BigDecimal.ZERO, roundOff = java.math.BigDecimal.ZERO;
    private Long invoiceTypeId, buildingId, floorId, allotmentId, refInvoiceId;
    private String placeOfSupply, referenceNumber, notes, terms;
    private boolean reverseCharge = false;
    private java.time.LocalDate periodFrom, periodTo, cancelledAt;
    public String getDocumentNumber(){return documentNumber;} public void setDocumentNumber(String v){documentNumber=v;}
    public Customer getTenant(){return tenant;} public void setTenant(Customer v){tenant=v;}
    public java.time.YearMonth getBillingPeriod(){return billingPeriod;} public void setBillingPeriod(java.time.YearMonth v){billingPeriod=v;}
    public java.time.LocalDate getDueDate(){return dueDate;} public void setDueDate(java.time.LocalDate v){dueDate=v;}
    public String getSupplyState(){return supplyState;} public void setSupplyState(String v){supplyState=v;}
    public DocumentStatus getStatus(){return status;} public void setStatus(DocumentStatus v){status=v;}
    public java.math.BigDecimal getSubtotal(){return subtotal;} public void setSubtotal(java.math.BigDecimal v){subtotal=v;}
    public java.math.BigDecimal getDiscount(){return discount;} public void setDiscount(java.math.BigDecimal v){discount=v;}
    public java.math.BigDecimal getCgst(){return cgst;} public void setCgst(java.math.BigDecimal v){cgst=v;}
    public java.math.BigDecimal getSgst(){return sgst;} public void setSgst(java.math.BigDecimal v){sgst=v;}
    public java.math.BigDecimal getIgst(){return igst;} public void setIgst(java.math.BigDecimal v){igst=v;}
    public java.math.BigDecimal getCess(){return cess;} public void setCess(java.math.BigDecimal v){cess=v;}
    public java.math.BigDecimal getTds(){return tds;} public void setTds(java.math.BigDecimal v){tds=v;}
    public java.math.BigDecimal getTotal(){return total;} public void setTotal(java.math.BigDecimal v){total=v;}
    public java.math.BigDecimal getTdsRate(){return tdsRate;} public void setTdsRate(java.math.BigDecimal v){tdsRate=v;}
    public java.math.BigDecimal getNetPayable(){return netPayable;} public void setNetPayable(java.math.BigDecimal v){netPayable=v;}
    public java.math.BigDecimal getRoundOff(){return roundOff;} public void setRoundOff(java.math.BigDecimal v){roundOff=v;}
    public Long getInvoiceTypeId(){return invoiceTypeId;} public void setInvoiceTypeId(Long v){invoiceTypeId=v;}
    public Long getBuildingId(){return buildingId;} public void setBuildingId(Long v){buildingId=v;}
    public Long getFloorId(){return floorId;} public void setFloorId(Long v){floorId=v;}
    public Long getAllotmentId(){return allotmentId;} public void setAllotmentId(Long v){allotmentId=v;}
    public Long getRefInvoiceId(){return refInvoiceId;} public void setRefInvoiceId(Long v){refInvoiceId=v;}
    public String getPlaceOfSupply(){return placeOfSupply;} public void setPlaceOfSupply(String v){placeOfSupply=v;}
    public String getReferenceNumber(){return referenceNumber;} public void setReferenceNumber(String v){referenceNumber=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public String getTerms(){return terms;} public void setTerms(String v){terms=v;}
    public boolean isReverseCharge(){return reverseCharge;} public void setReverseCharge(boolean v){reverseCharge=v;}
    public java.time.LocalDate getPeriodFrom(){return periodFrom;} public void setPeriodFrom(java.time.LocalDate v){periodFrom=v;}
    public java.time.LocalDate getPeriodTo(){return periodTo;} public void setPeriodTo(java.time.LocalDate v){periodTo=v;}
    public java.time.LocalDate getCancelledAt(){return cancelledAt;} public void setCancelledAt(java.time.LocalDate v){cancelledAt=v;}
}
