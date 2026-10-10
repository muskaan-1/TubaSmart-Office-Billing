package com.tubasmart.backend.controller;

import com.tubasmart.backend.model.SacHsnGst;
import com.tubasmart.backend.repository.SacHsnGstRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/masters/sac-hsn")
public class SacHsnGstController {
    private final SacHsnGstRepository repo;
    public SacHsnGstController(SacHsnGstRepository repo){this.repo=repo;}
    @GetMapping public List<SacHsnGst> all(){return repo.findAll();}
    @GetMapping("/{id}") public SacHsnGst get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
    @PostMapping public ResponseEntity<SacHsnGst> create(@RequestBody SacHsnGst x){return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(x));}
    @PutMapping("/{id}") public SacHsnGst update(@PathVariable Long id,@RequestBody SacHsnGst x){x.getClass(); var old=repo.findById(id).orElseThrow(); return repo.save(copyId(x,old));}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){repo.deleteById(id); return ResponseEntity.noContent().build();}
    private SacHsnGst copyId(SacHsnGst x,SacHsnGst old) {
        try { var f=com.tubasmart.backend.model.BaseEntity.class.getDeclaredField("id"); f.setAccessible(true); f.set(x, f.get(old)); } catch(Exception e){throw new RuntimeException(e);}
        return x;
    }
}
