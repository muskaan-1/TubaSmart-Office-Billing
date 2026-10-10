package com.tubasmart.backend.controller;

import com.tubasmart.backend.api.BadRequestException;
import com.tubasmart.backend.model.*;
import com.tubasmart.backend.repository.*;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.math.*;
import java.util.*;

@RestController @RequestMapping("/api/receipts/allocations")
public class ReceiptAllocationController {
    private final ReceiptAllocationRepository repo; private final ReceiptRepository receipts; private final InvoiceRepository invoices;
    public ReceiptAllocationController(ReceiptAllocationRepository r,ReceiptRepository receipts,InvoiceRepository invoices){this.repo=r;this.receipts=receipts;this.invoices=invoices;}
    @GetMapping public List<ReceiptAllocation> all(){return repo.findAll();}
    @GetMapping("/{id}") public ReceiptAllocation get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
    public record AllocationRequest(Long receiptId,Long invoiceId,BigDecimal amount){}
    @PostMapping @Transactional public ResponseEntity<ReceiptAllocation> create(@RequestBody AllocationRequest r){
        Receipt receipt=receipts.findById(r.receiptId()).orElseThrow(); Invoice invoice=invoices.findById(r.invoiceId()).orElseThrow(); BigDecimal amount=nvl(r.amount()).setScale(2,RoundingMode.HALF_UP);
        if(receipt.getStatus()==DocumentStatus.CANCELLED) throw new BadRequestException("RECEIPT_CANCELLED","Cannot allocate a cancelled receipt");
        if(invoice.getStatus()!=DocumentStatus.ISSUED) throw new BadRequestException("INVOICE_NOT_OPEN","Only issued invoices can receive allocations");
        if(!receipt.getTenant().getId().equals(invoice.getTenant().getId())) throw new BadRequestException("CUSTOMER_MISMATCH","Receipt and invoice belong to different customers");
        if(amount.signum()<=0) throw new BadRequestException("INVALID_ALLOCATION","Allocation amount must be greater than zero");
        BigDecimal already=repo.findByReceiptId(receipt.getId()).stream().map(ReceiptAllocation::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal invoicePaid=repo.sumAmountByInvoiceId(invoice.getId());
        BigDecimal invoiceValue=invoice.getNetPayable()==null||invoice.getNetPayable().signum()==0?invoice.getTotal():invoice.getNetPayable();
        if(already.add(amount).compareTo(receipt.getAmount())>0) throw new BadRequestException("RECEIPT_OVER_ALLOCATED","Allocation exceeds receipt amount");
        if(invoicePaid.add(amount).compareTo(invoiceValue)>0) throw new BadRequestException("INVOICE_OVER_ALLOCATED","Allocation exceeds invoice outstanding amount");
        ReceiptAllocation a=new ReceiptAllocation();a.setReceipt(receipt);a.setInvoice(invoice);a.setAmount(amount);ReceiptAllocation saved=repo.save(a);
        receipt.setUnallocatedAmount(receipt.getAmount().subtract(already).subtract(amount).max(BigDecimal.ZERO).setScale(2,RoundingMode.HALF_UP));receipts.save(receipt);return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @DeleteMapping("/{id}") @Transactional public ResponseEntity<Void> delete(@PathVariable Long id){ReceiptAllocation a=repo.findById(id).orElseThrow();Receipt r=a.getReceipt();repo.delete(a);BigDecimal allocated=repo.findByReceiptId(r.getId()).stream().map(ReceiptAllocation::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);r.setUnallocatedAmount(r.getAmount().subtract(allocated).max(BigDecimal.ZERO).setScale(2,RoundingMode.HALF_UP));receipts.save(r);return ResponseEntity.noContent().build();}
    private BigDecimal nvl(BigDecimal x){return x==null?BigDecimal.ZERO:x;}
}
