package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="floor_prices")
public class FloorPrice extends BaseEntity {
    @ManyToOne(optional=false) private Floor floor;
    @Column(nullable=false) private String chargeType;
    private java.math.BigDecimal fixedRate, perSqFtRate;
    @Column(nullable=false) private java.time.LocalDate effectiveDate;
    private java.math.BigDecimal escalationPercent = java.math.BigDecimal.ZERO;
    private String rateBasis, remarks;
    private boolean active = true;
    public Floor getFloor(){return floor;} public void setFloor(Floor v){floor=v;}
    public String getChargeType(){return chargeType;} public void setChargeType(String v){chargeType=v;}
    public java.math.BigDecimal getFixedRate(){return fixedRate;} public void setFixedRate(java.math.BigDecimal v){fixedRate=v;}
    public java.math.BigDecimal getPerSqFtRate(){return perSqFtRate;} public void setPerSqFtRate(java.math.BigDecimal v){perSqFtRate=v;}
    public java.time.LocalDate getEffectiveDate(){return effectiveDate;} public void setEffectiveDate(java.time.LocalDate v){effectiveDate=v;}
    public java.math.BigDecimal getEscalationPercent(){return escalationPercent;} public void setEscalationPercent(java.math.BigDecimal v){escalationPercent=v;}
    public String getRateBasis(){return rateBasis;} public void setRateBasis(String v){rateBasis=v;}
    public String getRemarks(){return remarks;} public void setRemarks(String v){remarks=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
