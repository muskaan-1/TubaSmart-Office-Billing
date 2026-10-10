package com.tubasmart.backend.controller;

import com.tubasmart.backend.model.Floor;
import com.tubasmart.backend.repository.FloorRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/masters/floors")
public class FloorController {
    private final FloorRepository repo;
    public FloorController(FloorRepository repo){this.repo=repo;}
    @GetMapping public List<Floor> all(){return repo.findAll();}
    @GetMapping("/{id}") public Floor get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
    @PostMapping public ResponseEntity<Floor> create(@RequestBody Floor x){return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(x));}
    @PutMapping("/{id}") public Floor update(@PathVariable Long id,@RequestBody Floor x){x.getClass(); var old=repo.findById(id).orElseThrow(); return repo.save(copyId(x,old));}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){repo.deleteById(id); return ResponseEntity.noContent().build();}
    private Floor copyId(Floor x,Floor old) {
        try { var f=com.tubasmart.backend.model.BaseEntity.class.getDeclaredField("id"); f.setAccessible(true); f.set(x, f.get(old)); } catch(Exception e){throw new RuntimeException(e);}
        return x;
    }
}
