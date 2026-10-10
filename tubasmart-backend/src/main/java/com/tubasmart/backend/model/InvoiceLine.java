package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="invoice_lines")
public class InvoiceLine extends BaseEntity {
    @ManyToOne(optional=false) private Invoice invoice;
    private String chargeType, description, sacHsnCode;
    private java.math.BigDecimal quantity, rate, amount, taxRate, taxAmount, discount, taxable, cgst, sgst, igst, cess;
    private Long chargeTypeId, sacHsnGstId;
    public Invoice getInvoice(){return invoice;} public void setInvoice(Invoice v){invoice=v;}
    public String getChargeType(){return chargeType;} public void setChargeType(String v){chargeType=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getSacHsnCode(){return sacHsnCode;} public void setSacHsnCode(String v){sacHsnCode=v;}
    public java.math.BigDecimal getQuantity(){return quantity;} public void setQuantity(java.math.BigDecimal v){quantity=v;}
    public java.math.BigDecimal getRate(){return rate;} public void setRate(java.math.BigDecimal v){rate=v;}
    public java.math.BigDecimal getAmount(){return amount;} public void setAmount(java.math.BigDecimal v){amount=v;}
    public java.math.BigDecimal getTaxRate(){return taxRate;} public void setTaxRate(java.math.BigDecimal v){taxRate=v;}
    public java.math.BigDecimal getTaxAmount(){return taxAmount;} public void setTaxAmount(java.math.BigDecimal v){taxAmount=v;}
    public java.math.BigDecimal getDiscount(){return discount;} public void setDiscount(java.math.BigDecimal v){discount=v;}
    public java.math.BigDecimal getTaxable(){return taxable;} public void setTaxable(java.math.BigDecimal v){taxable=v;}
    public java.math.BigDecimal getCgst(){return cgst;} public void setCgst(java.math.BigDecimal v){cgst=v;}
    public java.math.BigDecimal getSgst(){return sgst;} public void setSgst(java.math.BigDecimal v){sgst=v;}
    public java.math.BigDecimal getIgst(){return igst;} public void setIgst(java.math.BigDecimal v){igst=v;}
    public java.math.BigDecimal getCess(){return cess;} public void setCess(java.math.BigDecimal v){cess=v;}
    public Long getChargeTypeId(){return chargeTypeId;} public void setChargeTypeId(Long v){chargeTypeId=v;}
    public Long getSacHsnGstId(){return sacHsnGstId;} public void setSacHsnGstId(Long v){sacHsnGstId=v;}
}
