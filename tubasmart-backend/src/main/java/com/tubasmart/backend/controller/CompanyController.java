package com.tubasmart.backend.controller;

import com.tubasmart.backend.model.Company;
import com.tubasmart.backend.repository.CompanyRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    private final CompanyRepository repo;
    public CompanyController(CompanyRepository repo){this.repo=repo;}
    @GetMapping public List<Company> all(){return repo.findAll();}
    @GetMapping("/{id}") public Company get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
    @PostMapping public ResponseEntity<Company> create(@RequestBody Company x){return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(x));}
    @PutMapping("/{id}") public Company update(@PathVariable Long id,@RequestBody Company x){x.getClass(); var old=repo.findById(id).orElseThrow(); return repo.save(copyId(x,old));}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){repo.deleteById(id); return ResponseEntity.noContent().build();}
    private Company copyId(Company x,Company old) {
        try { var f=com.tubasmart.backend.model.BaseEntity.class.getDeclaredField("id"); f.setAccessible(true); f.set(x, f.get(old)); } catch(Exception e){throw new RuntimeException(e);}
        return x;
    }
}
