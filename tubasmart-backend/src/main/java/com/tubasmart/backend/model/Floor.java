package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="floors")
public class Floor extends BaseEntity {
    @ManyToOne(optional=false) private Building building;
    @Column(nullable=false) private String floorName;
    private java.math.BigDecimal carpetArea, chargeableArea;
    private String usage, workingHours;
    @Enumerated(EnumType.STRING) private FloorStatus status = FloorStatus.VACANT_READY;
    private boolean active = true;
    public Building getBuilding(){return building;} public void setBuilding(Building v){building=v;}
    public String getFloorName(){return floorName;} public void setFloorName(String v){floorName=v;}
    public java.math.BigDecimal getCarpetArea(){return carpetArea;} public void setCarpetArea(java.math.BigDecimal v){carpetArea=v;}
    public java.math.BigDecimal getChargeableArea(){return chargeableArea;} public void setChargeableArea(java.math.BigDecimal v){chargeableArea=v;}
    public String getUsage(){return usage;} public void setUsage(String v){usage=v;}
    public String getWorkingHours(){return workingHours;} public void setWorkingHours(String v){workingHours=v;}
    public FloorStatus getStatus(){return status;} public void setStatus(FloorStatus v){status=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
