package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="tenant_charge_schedule")
public class TenantChargeSchedule extends BaseEntity {
    @ManyToOne(optional=false) private TenantAllotment allotment;
    @Column(nullable=false) private String chargeType;
    private java.math.BigDecimal overrideAmount, actualAmount, rate = java.math.BigDecimal.ZERO;
    private String basis, remarks;
    private boolean autoBill = true;
    private boolean active = true;
    public TenantAllotment getAllotment(){return allotment;} public void setAllotment(TenantAllotment v){allotment=v;}
    public String getChargeType(){return chargeType;} public void setChargeType(String v){chargeType=v;}
    public java.math.BigDecimal getOverrideAmount(){return overrideAmount;} public void setOverrideAmount(java.math.BigDecimal v){overrideAmount=v;}
    public java.math.BigDecimal getActualAmount(){return actualAmount;} public void setActualAmount(java.math.BigDecimal v){actualAmount=v;}
    public java.math.BigDecimal getRate(){return rate;} public void setRate(java.math.BigDecimal v){rate=v;}
    public String getBasis(){return basis;} public void setBasis(String v){basis=v;}
    public String getRemarks(){return remarks;} public void setRemarks(String v){remarks=v;}
    public boolean isAutoBill(){return autoBill;} public void setAutoBill(boolean v){autoBill=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
