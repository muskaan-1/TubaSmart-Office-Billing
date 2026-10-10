package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="receipt_allocations")
public class ReceiptAllocation extends BaseEntity {
    @ManyToOne(optional=false) private Receipt receipt;
    @ManyToOne(optional=false) private Invoice invoice;
    @Column(nullable=false) private java.math.BigDecimal amount;
    public Receipt getReceipt(){return receipt;} public void setReceipt(Receipt v){receipt=v;}
    public Invoice getInvoice(){return invoice;} public void setInvoice(Invoice v){invoice=v;}
    public java.math.BigDecimal getAmount(){return amount;} public void setAmount(java.math.BigDecimal v){amount=v;}
}
