package com.tubasmart.backend.model;

import jakarta.persistence.*;
@Entity @Table(name="companies")
public class Company extends BaseEntity {
    @Column(nullable=false) private String name;
    private String gstin, panCin, bankDetails, upi, logoPath, signatory;
    private String legalName, address1, address2, city, stateCode, pin, email, phone, website, bankName, accountNo, ifsc, branch, signName, signDesignation, invPrefix;
    private boolean gstEnabled = true;
    private int defaultCreditDays = 30;
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getLegalName(){return legalName;} public void setLegalName(String v){legalName=v;}
    public String getAddress1(){return address1;} public void setAddress1(String v){address1=v;}
    public String getAddress2(){return address2;} public void setAddress2(String v){address2=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;}
    public String getStateCode(){return stateCode;} public void setStateCode(String v){stateCode=v;}
    public String getPin(){return pin;} public void setPin(String v){pin=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getWebsite(){return website;} public void setWebsite(String v){website=v;}
    public String getBankName(){return bankName;} public void setBankName(String v){bankName=v;}
    public String getAccountNo(){return accountNo;} public void setAccountNo(String v){accountNo=v;}
    public String getIfsc(){return ifsc;} public void setIfsc(String v){ifsc=v;}
    public String getBranch(){return branch;} public void setBranch(String v){branch=v;}
    public String getSignName(){return signName;} public void setSignName(String v){signName=v;}
    public String getSignDesignation(){return signDesignation;} public void setSignDesignation(String v){signDesignation=v;}
    public String getInvPrefix(){return invPrefix;} public void setInvPrefix(String v){invPrefix=v;}
    public boolean isGstEnabled(){return gstEnabled;} public void setGstEnabled(boolean v){gstEnabled=v;}
    public int getDefaultCreditDays(){return defaultCreditDays;} public void setDefaultCreditDays(int v){defaultCreditDays=v;}
    public String getGstin(){return gstin;} public void setGstin(String v){gstin=v;}
    public String getPanCin(){return panCin;} public void setPanCin(String v){panCin=v;}
    public String getBankDetails(){return bankDetails;} public void setBankDetails(String v){bankDetails=v;}
    public String getUpi(){return upi;} public void setUpi(String v){upi=v;}
    public String getLogoPath(){return logoPath;} public void setLogoPath(String v){logoPath=v;}
    public String getSignatory(){return signatory;} public void setSignatory(String v){signatory=v;}
}
