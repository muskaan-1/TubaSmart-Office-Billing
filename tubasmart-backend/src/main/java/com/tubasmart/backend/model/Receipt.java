package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="receipts")
public class Receipt extends BaseEntity {
    @Column(nullable=false, unique=true) private String receiptNumber;
    @ManyToOne(optional=false) private Customer tenant;
    private java.time.LocalDate receiptDate;
    @Enumerated(EnumType.STRING) private PaymentMode mode;
    private java.math.BigDecimal amount = java.math.BigDecimal.ZERO;
    private java.math.BigDecimal unallocatedAmount = java.math.BigDecimal.ZERO;
    private String reference, refDate, bank, narration;
    private java.math.BigDecimal tdsDeducted = java.math.BigDecimal.ZERO;
    @Enumerated(EnumType.STRING) private DocumentStatus status = DocumentStatus.ISSUED;
    public String getReceiptNumber(){return receiptNumber;} public void setReceiptNumber(String v){receiptNumber=v;}
    public Customer getTenant(){return tenant;} public void setTenant(Customer v){tenant=v;}
    public java.time.LocalDate getReceiptDate(){return receiptDate;} public void setReceiptDate(java.time.LocalDate v){receiptDate=v;}
    public PaymentMode getMode(){return mode;} public void setMode(PaymentMode v){mode=v;}
    public java.math.BigDecimal getAmount(){return amount;} public void setAmount(java.math.BigDecimal v){amount=v;}
    public java.math.BigDecimal getUnallocatedAmount(){return unallocatedAmount;} public void setUnallocatedAmount(java.math.BigDecimal v){unallocatedAmount=v;}
    public String getReference(){return reference;} public void setReference(String v){reference=v;}
    public String getRefDate(){return refDate;} public void setRefDate(String v){refDate=v;}
    public String getBank(){return bank;} public void setBank(String v){bank=v;}
    public String getNarration(){return narration;} public void setNarration(String v){narration=v;}
    public java.math.BigDecimal getTdsDeducted(){return tdsDeducted;} public void setTdsDeducted(java.math.BigDecimal v){tdsDeducted=v;}
    public DocumentStatus getStatus(){return status;} public void setStatus(DocumentStatus v){status=v;}
}
