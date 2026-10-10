package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="charge_types")
public class ChargeType extends BaseEntity {
    @Column(nullable=false, unique=true) private String name;
    private String code, sacHsnCode, unit, defaultDescription;
    private boolean isRecurring = true;
    private boolean active = true;
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getCode(){return code;} public void setCode(String v){code=v;}
    public boolean isRecurring(){return isRecurring;} public void setRecurring(boolean v){isRecurring=v;}
    public String getSacHsnCode(){return sacHsnCode;} public void setSacHsnCode(String v){sacHsnCode=v;}
    public String getUnit(){return unit;} public void setUnit(String v){unit=v;}
    public String getDefaultDescription(){return defaultDescription;} public void setDefaultDescription(String v){defaultDescription=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
