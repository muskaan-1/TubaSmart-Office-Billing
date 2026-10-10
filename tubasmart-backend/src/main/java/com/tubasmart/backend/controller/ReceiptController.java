package com.tubasmart.backend.controller;
import com.tubasmart.backend.api.ConflictException;
import com.tubasmart.backend.model.*; import com.tubasmart.backend.repository.*; import com.tubasmart.backend.service.ReceiptService;
import org.springframework.http.*; import org.springframework.transaction.annotation.Transactional; import org.springframework.web.bind.annotation.*;
import java.math.*; import java.time.*; import java.util.*;
@RestController @RequestMapping("/api/receipts") public class ReceiptController {
 private final ReceiptRepository repo; private final ReceiptService service; private final ReceiptAllocationRepository allocations;
 public ReceiptController(ReceiptRepository r,ReceiptService s,ReceiptAllocationRepository a){repo=r;service=s;allocations=a;}
 @GetMapping public List<Receipt> all(){return repo.findAll();}
 @GetMapping("/{id}") public Receipt get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
 public record CreateRequest(Long tenantId,LocalDate receiptDate,PaymentMode mode,BigDecimal amount,String reference){}
 @PostMapping public ResponseEntity<Receipt> create(@RequestBody CreateRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r.tenantId(),r.receiptDate(),r.mode(),r.amount(),r.reference()));}
 /** Amount, tenant, number, status and unallocated balance are not editable; cancel and re-issue instead. */
 public record UpdateRequest(LocalDate receiptDate,PaymentMode mode,String reference,String refDate,String bank,String narration){}
 @PutMapping("/{id}") public Receipt update(@PathVariable Long id,@RequestBody UpdateRequest r){
  Receipt x=repo.findById(id).orElseThrow();
  if(x.getStatus()==DocumentStatus.CANCELLED) throw new ConflictException("Cancelled receipts cannot be edited");
  if(r.receiptDate()!=null) x.setReceiptDate(r.receiptDate());
  if(r.mode()!=null) x.setMode(r.mode());
  if(r.reference()!=null) x.setReference(r.reference());
  if(r.refDate()!=null) x.setRefDate(r.refDate());
  if(r.bank()!=null) x.setBank(r.bank());
  if(r.narration()!=null) x.setNarration(r.narration());
  return repo.save(x);
 }
 @PostMapping("/{id}/cancel") @Transactional public Receipt cancel(@PathVariable Long id){
  Receipt x=repo.findById(id).orElseThrow();
  if(x.getStatus()==DocumentStatus.CANCELLED) return x;
  if(!allocations.findByReceiptId(id).isEmpty()) throw new ConflictException("Receipt has allocations; remove them before cancelling");
  x.setStatus(DocumentStatus.CANCELLED); x.setUnallocatedAmount(BigDecimal.ZERO.setScale(2));
  return repo.save(x);
 }
 @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){throw new ConflictException("Receipts must be cancelled; they cannot be deleted");}
}
