package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="tenant_allotments")
public class TenantAllotment extends BaseEntity {
    @ManyToOne(optional=false) private Customer tenant;
    @ManyToOne(optional=false) private Floor floor;
    @Column(nullable=false) private String agreementNumber;
    private java.time.LocalDate occupancyStart, occupancyEnd;
    private java.math.BigDecimal deposit = java.math.BigDecimal.ZERO;
    private java.math.BigDecimal discount = java.math.BigDecimal.ZERO, discountPct = java.math.BigDecimal.ZERO, escalationPct = java.math.BigDecimal.ZERO;
    private String remarks, agreementFile;
    private int billingDay = 1;
    private int lockInMonths;
    private boolean active = true;
    public Customer getTenant(){return tenant;} public void setTenant(Customer v){tenant=v;}
    public Floor getFloor(){return floor;} public void setFloor(Floor v){floor=v;}
    public String getAgreementNumber(){return agreementNumber;} public void setAgreementNumber(String v){agreementNumber=v;}
    public java.time.LocalDate getOccupancyStart(){return occupancyStart;} public void setOccupancyStart(java.time.LocalDate v){occupancyStart=v;}
    public java.time.LocalDate getOccupancyEnd(){return occupancyEnd;} public void setOccupancyEnd(java.time.LocalDate v){occupancyEnd=v;}
    public java.math.BigDecimal getDeposit(){return deposit;} public void setDeposit(java.math.BigDecimal v){deposit=v;}
    public java.math.BigDecimal getDiscount(){return discount;} public void setDiscount(java.math.BigDecimal v){discount=v;}
    public java.math.BigDecimal getDiscountPct(){return discountPct;} public void setDiscountPct(java.math.BigDecimal v){discountPct=v;}
    public java.math.BigDecimal getEscalationPct(){return escalationPct;} public void setEscalationPct(java.math.BigDecimal v){escalationPct=v;}
    public String getRemarks(){return remarks;} public void setRemarks(String v){remarks=v;}
    public String getAgreementFile(){return agreementFile;} public void setAgreementFile(String v){agreementFile=v;}
    public int getBillingDay(){return billingDay;} public void setBillingDay(int v){billingDay=v;}
    public int getLockInMonths(){return lockInMonths;} public void setLockInMonths(int v){lockInMonths=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
