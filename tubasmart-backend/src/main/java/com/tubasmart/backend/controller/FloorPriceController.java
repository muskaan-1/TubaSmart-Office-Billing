package com.tubasmart.backend.controller;

import com.tubasmart.backend.model.FloorPrice;
import com.tubasmart.backend.repository.FloorPriceRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/masters/floor-prices")
public class FloorPriceController {
    private final FloorPriceRepository repo;
    public FloorPriceController(FloorPriceRepository repo){this.repo=repo;}
    @GetMapping public List<FloorPrice> all(){return repo.findAll();}
    @GetMapping("/{id}") public FloorPrice get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
    @PostMapping public ResponseEntity<FloorPrice> create(@RequestBody FloorPrice x){return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(x));}
    @PutMapping("/{id}") public FloorPrice update(@PathVariable Long id,@RequestBody FloorPrice x){x.getClass(); var old=repo.findById(id).orElseThrow(); return repo.save(copyId(x,old));}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){repo.deleteById(id); return ResponseEntity.noContent().build();}
    private FloorPrice copyId(FloorPrice x,FloorPrice old) {
        try { var f=com.tubasmart.backend.model.BaseEntity.class.getDeclaredField("id"); f.setAccessible(true); f.set(x, f.get(old)); } catch(Exception e){throw new RuntimeException(e);}
        return x;
    }
}
