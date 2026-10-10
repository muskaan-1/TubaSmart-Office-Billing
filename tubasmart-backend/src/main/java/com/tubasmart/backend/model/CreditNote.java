package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="credit_notes")
public class CreditNote extends BaseEntity {
    @Column(nullable=false, unique=true) private String documentNumber;
    @ManyToOne(optional=false) private Invoice originalInvoice;
    @Enumerated(EnumType.STRING) private DocumentStatus status = DocumentStatus.DRAFT;
    private java.math.BigDecimal taxableValue = java.math.BigDecimal.ZERO, taxImpact = java.math.BigDecimal.ZERO, total = java.math.BigDecimal.ZERO;
    private String reason, refNo;
    private java.time.LocalDate date;
    public String getDocumentNumber(){return documentNumber;} public void setDocumentNumber(String v){documentNumber=v;}
    public Invoice getOriginalInvoice(){return originalInvoice;} public void setOriginalInvoice(Invoice v){originalInvoice=v;}
    public DocumentStatus getStatus(){return status;} public void setStatus(DocumentStatus v){status=v;}
    public java.math.BigDecimal getTaxableValue(){return taxableValue;} public void setTaxableValue(java.math.BigDecimal v){taxableValue=v;}
    public java.math.BigDecimal getTaxImpact(){return taxImpact;} public void setTaxImpact(java.math.BigDecimal v){taxImpact=v;}
    public java.math.BigDecimal getTotal(){return total;} public void setTotal(java.math.BigDecimal v){total=v;}
    public String getReason(){return reason;} public void setReason(String v){reason=v;}
    public String getRefNo(){return refNo;} public void setRefNo(String v){refNo=v;}
    public java.time.LocalDate getDate(){return date;} public void setDate(java.time.LocalDate v){date=v;}
}
