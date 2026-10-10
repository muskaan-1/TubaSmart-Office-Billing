package com.tubasmart.backend.controller;

import com.tubasmart.backend.api.BadRequestException;
import com.tubasmart.backend.api.ConflictException;
import com.tubasmart.backend.model.*;
import com.tubasmart.backend.repository.*;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController @RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceRepository repo; private final InvoiceLineRepository lines; private final ReceiptAllocationRepository allocations;
    public InvoiceController(InvoiceRepository repo,InvoiceLineRepository lines,ReceiptAllocationRepository allocations){this.repo=repo;this.lines=lines;this.allocations=allocations;}
    @GetMapping public List<Invoice> all(){return repo.findAll();}
    @GetMapping("/{id}") public Invoice get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
    @GetMapping("/{id}/lines") public List<InvoiceLine> getLines(@PathVariable Long id){return lines.findByInvoiceId(id);}

    /** Invoices are produced by the billing engine. Manual creation is limited to a DRAFT shell. */
    @PostMapping public ResponseEntity<Invoice> create(@RequestBody Invoice x){
        if(x.getTenant()==null||x.getTenant().getId()==null) throw new BadRequestException("TENANT_REQUIRED","tenant.id is required");
        if(x.getDocumentNumber()==null||x.getDocumentNumber().isBlank()) throw new BadRequestException("DOCUMENT_NUMBER_REQUIRED","documentNumber is required");
        x.setStatus(DocumentStatus.DRAFT);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(x));
    }

    /** Only non-financial fields can be edited; totals, tax, number, status and tenant are server-controlled. */
    public record UpdateRequest(LocalDate dueDate,String referenceNumber,String notes,String terms,String placeOfSupply){}
    @PutMapping("/{id}") public Invoice update(@PathVariable Long id,@RequestBody UpdateRequest r){
        Invoice inv=repo.findById(id).orElseThrow();
        if(inv.getStatus()==DocumentStatus.CANCELLED) throw new ConflictException("Cancelled invoices cannot be edited");
        if(r.dueDate()!=null) inv.setDueDate(r.dueDate());
        if(r.referenceNumber()!=null) inv.setReferenceNumber(r.referenceNumber());
        if(r.notes()!=null) inv.setNotes(r.notes());
        if(r.terms()!=null) inv.setTerms(r.terms());
        if(r.placeOfSupply()!=null) inv.setPlaceOfSupply(r.placeOfSupply());
        return repo.save(inv);
    }

    @PostMapping("/{id}/cancel") @Transactional public Invoice cancel(@PathVariable Long id){
        Invoice x=repo.findById(id).orElseThrow();
        if(x.getStatus()==DocumentStatus.CANCELLED) return x;
        if(!allocations.findByInvoiceId(id).isEmpty()) throw new ConflictException("Invoice has receipts allocated; remove the allocations before cancelling");
        x.setStatus(DocumentStatus.CANCELLED); x.setCancelledAt(LocalDate.now());
        return repo.save(x);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){throw new ConflictException("Issued invoices must be cancelled; they cannot be deleted");}
}
