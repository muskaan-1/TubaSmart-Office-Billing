package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="invoice_types")
public class InvoiceType extends BaseEntity {
    @Column(nullable=false, unique=true) private String name;
    private String prefix, financialYearSeries, terms;
    private int creditDays;
    private String nature = "SALE";
    private boolean active = true;
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getPrefix(){return prefix;} public void setPrefix(String v){prefix=v;}
    public String getFinancialYearSeries(){return financialYearSeries;} public void setFinancialYearSeries(String v){financialYearSeries=v;}
    public String getTerms(){return terms;} public void setTerms(String v){terms=v;}
    public int getCreditDays(){return creditDays;} public void setCreditDays(int v){creditDays=v;}
    public String getNature(){return nature;} public void setNature(String v){nature=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
