package com.tubasmart.backend.controller;

import com.tubasmart.backend.model.ChargeType;
import com.tubasmart.backend.repository.ChargeTypeRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/masters/charge-types")
public class ChargeTypeController {
    private final ChargeTypeRepository repo;
    public ChargeTypeController(ChargeTypeRepository repo){this.repo=repo;}
    @GetMapping public List<ChargeType> all(){return repo.findAll();}
    @GetMapping("/{id}") public ChargeType get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
    @PostMapping public ResponseEntity<ChargeType> create(@RequestBody ChargeType x){return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(x));}
    @PutMapping("/{id}") public ChargeType update(@PathVariable Long id,@RequestBody ChargeType x){x.getClass(); var old=repo.findById(id).orElseThrow(); return repo.save(copyId(x,old));}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){repo.deleteById(id); return ResponseEntity.noContent().build();}
    private ChargeType copyId(ChargeType x,ChargeType old) {
        try { var f=com.tubasmart.backend.model.BaseEntity.class.getDeclaredField("id"); f.setAccessible(true); f.set(x, f.get(old)); } catch(Exception e){throw new RuntimeException(e);}
        return x;
    }
}
