package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="customers")
public class Customer extends BaseEntity {
    @Column(nullable=false, unique=true) private String code;
    @Column(nullable=false) private String name;
    private String contact, state, gstin, pan, openingBalance;
    private int creditDays;
    private java.math.BigDecimal tdsRate = java.math.BigDecimal.ZERO;
    private boolean active = true;
    public String getCode(){return code;} public void setCode(String v){code=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getContact(){return contact;} public void setContact(String v){contact=v;}
    public String getState(){return state;} public void setState(String v){state=v;}
    public String getGstin(){return gstin;} public void setGstin(String v){gstin=v;}
    public String getPan(){return pan;} public void setPan(String v){pan=v;}
    public String getOpeningBalance(){return openingBalance;} public void setOpeningBalance(String v){openingBalance=v;}
    public int getCreditDays(){return creditDays;} public void setCreditDays(int v){creditDays=v;}
    public java.math.BigDecimal getTdsRate(){return tdsRate;} public void setTdsRate(java.math.BigDecimal v){tdsRate=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
