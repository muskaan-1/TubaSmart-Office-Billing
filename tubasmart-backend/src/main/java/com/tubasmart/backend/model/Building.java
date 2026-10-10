package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="buildings")
public class Building extends BaseEntity {
    @Column(nullable=false, unique=true) private String code;
    @Column(nullable=false) private String name;
    private String address, state, municipalNumber, owner;
    private java.math.BigDecimal plotArea;
    private boolean active = true;
    public String getCode(){return code;} public void setCode(String v){code=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
    public String getState(){return state;} public void setState(String v){state=v;}
    public String getMunicipalNumber(){return municipalNumber;} public void setMunicipalNumber(String v){municipalNumber=v;}
    public String getOwner(){return owner;} public void setOwner(String v){owner=v;}
    public java.math.BigDecimal getPlotArea(){return plotArea;} public void setPlotArea(java.math.BigDecimal v){plotArea=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
