package com.tubasmart.backend.controller;

import com.tubasmart.backend.model.TenantAllotment;
import com.tubasmart.backend.repository.TenantAllotmentRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/masters/allotments")
public class TenantAllotmentController {
    private final TenantAllotmentRepository repo;
    public TenantAllotmentController(TenantAllotmentRepository repo){this.repo=repo;}
    @GetMapping public List<TenantAllotment> all(){return repo.findAll();}
    @GetMapping("/{id}") public TenantAllotment get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
    @PostMapping public ResponseEntity<TenantAllotment> create(@RequestBody TenantAllotment x){return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(x));}
    @PutMapping("/{id}") public TenantAllotment update(@PathVariable Long id,@RequestBody TenantAllotment x){x.getClass(); var old=repo.findById(id).orElseThrow(); return repo.save(copyId(x,old));}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){repo.deleteById(id); return ResponseEntity.noContent().build();}
    private TenantAllotment copyId(TenantAllotment x,TenantAllotment old) {
        try { var f=com.tubasmart.backend.model.BaseEntity.class.getDeclaredField("id"); f.setAccessible(true); f.set(x, f.get(old)); } catch(Exception e){throw new RuntimeException(e);}
        return x;
    }
}
