package com.tubasmart.backend.controller;

import com.tubasmart.backend.model.InvoiceType;
import com.tubasmart.backend.repository.InvoiceTypeRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/masters/invoice-types")
public class InvoiceTypeController {
    private final InvoiceTypeRepository repo;
    public InvoiceTypeController(InvoiceTypeRepository repo){this.repo=repo;}
    @GetMapping public List<InvoiceType> all(){return repo.findAll();}
    @GetMapping("/{id}") public InvoiceType get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
    @PostMapping public ResponseEntity<InvoiceType> create(@RequestBody InvoiceType x){return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(x));}
    @PutMapping("/{id}") public InvoiceType update(@PathVariable Long id,@RequestBody InvoiceType x){x.getClass(); var old=repo.findById(id).orElseThrow(); return repo.save(copyId(x,old));}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){repo.deleteById(id); return ResponseEntity.noContent().build();}
    private InvoiceType copyId(InvoiceType x,InvoiceType old) {
        try { var f=com.tubasmart.backend.model.BaseEntity.class.getDeclaredField("id"); f.setAccessible(true); f.set(x, f.get(old)); } catch(Exception e){throw new RuntimeException(e);}
        return x;
    }
}
