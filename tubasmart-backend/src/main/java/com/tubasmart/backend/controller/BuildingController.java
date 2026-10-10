package com.tubasmart.backend.controller;

import com.tubasmart.backend.model.Building;
import com.tubasmart.backend.repository.BuildingRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/masters/buildings")
public class BuildingController {
    private final BuildingRepository repo;
    public BuildingController(BuildingRepository repo){this.repo=repo;}
    @GetMapping public List<Building> all(){return repo.findAll();}
    @GetMapping("/{id}") public Building get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
    @PostMapping public ResponseEntity<Building> create(@RequestBody Building x){return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(x));}
    @PutMapping("/{id}") public Building update(@PathVariable Long id,@RequestBody Building x){x.getClass(); var old=repo.findById(id).orElseThrow(); return repo.save(copyId(x,old));}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){repo.deleteById(id); return ResponseEntity.noContent().build();}
    private Building copyId(Building x,Building old) {
        try { var f=com.tubasmart.backend.model.BaseEntity.class.getDeclaredField("id"); f.setAccessible(true); f.set(x, f.get(old)); } catch(Exception e){throw new RuntimeException(e);}
        return x;
    }
}
