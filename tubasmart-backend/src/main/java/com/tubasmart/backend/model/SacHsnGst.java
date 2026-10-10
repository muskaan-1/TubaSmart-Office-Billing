package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="sac_hsn_gst")
public class SacHsnGst extends BaseEntity {
    @Column(nullable=false, unique=true) private String code;
    private String description, type;
    private boolean active = true;
    private boolean applicable = true;
    private java.math.BigDecimal rate = java.math.BigDecimal.ZERO;
    private java.math.BigDecimal cess = java.math.BigDecimal.ZERO;
    public String getCode(){return code;} public void setCode(String v){code=v;}
    public String getType(){return type;} public void setType(String v){type=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public boolean isApplicable(){return applicable;} public void setApplicable(boolean v){applicable=v;}
    public java.math.BigDecimal getRate(){return rate;} public void setRate(java.math.BigDecimal v){rate=v;}
    public java.math.BigDecimal getCess(){return cess;} public void setCess(java.math.BigDecimal v){cess=v;}
}
