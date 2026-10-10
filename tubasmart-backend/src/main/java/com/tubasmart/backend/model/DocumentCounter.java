package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="document_counters", uniqueConstraints=@UniqueConstraint(columnNames={"documentType","financialYear"}))
public class DocumentCounter extends BaseEntity {
    @Column(nullable=false) private String documentType;
    @Column(nullable=false) private String financialYear;
    private long lastNumber = 0;
    public String getDocumentType(){return documentType;} public void setDocumentType(String v){documentType=v;}
    public String getFinancialYear(){return financialYear;} public void setFinancialYear(String v){financialYear=v;}
    public long getLastNumber(){return lastNumber;} public void setLastNumber(long v){lastNumber=v;}
}
