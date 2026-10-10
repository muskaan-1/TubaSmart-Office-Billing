package com.tubasmart.backend.service;

import com.tubasmart.backend.api.BadRequestException;
import com.tubasmart.backend.model.*;
import com.tubasmart.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*; import java.time.*;

@Service
public class ReceiptService {
 private final ReceiptRepository receipts; private final CustomerRepository customers; private final DocumentCounterRepository counters;
 public ReceiptService(ReceiptRepository r,CustomerRepository c,DocumentCounterRepository d){receipts=r;customers=c;counters=d;}
 @Transactional public Receipt create(Long tenantId,LocalDate date,PaymentMode mode,BigDecimal amount,String reference){
  if(tenantId==null) throw new BadRequestException("TENANT_REQUIRED","tenantId is required");
  Customer tenant=customers.findById(tenantId).orElseThrow(()->new BadRequestException("TENANT_NOT_FOUND","Customer not found")); BigDecimal a=money(amount);if(a.signum()<=0)throw new BadRequestException("INVALID_RECEIPT_AMOUNT","Receipt amount must be greater than zero");
  Receipt r=new Receipt();r.setTenant(tenant);r.setReceiptDate(date==null?LocalDate.now():date);r.setMode(mode);r.setAmount(a);r.setUnallocatedAmount(a);r.setReference(reference);r.setStatus(DocumentStatus.ISSUED);r.setReceiptNumber(next(r.getReceiptDate()));return receipts.save(r);
 }
 private String next(LocalDate d){int y=d.getMonthValue()>=4?d.getYear():d.getYear()-1;String fy=y+"-"+String.format("%02d",(y+1)%100);var c=counters.lockByTypeAndYear("RCT",fy).orElseGet(()->{var x=new DocumentCounter();x.setDocumentType("RCT");x.setFinancialYear(fy);return x;});c.setLastNumber(c.getLastNumber()+1);counters.save(c);return "RCT/"+fy+"/"+String.format("%05d",c.getLastNumber());}
 private BigDecimal money(BigDecimal x){return (x==null?BigDecimal.ZERO:x).setScale(2,RoundingMode.HALF_UP);}
}
