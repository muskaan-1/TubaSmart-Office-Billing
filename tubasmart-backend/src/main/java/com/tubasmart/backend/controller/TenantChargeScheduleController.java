package com.tubasmart.backend.controller;

import com.tubasmart.backend.model.TenantChargeSchedule;
import com.tubasmart.backend.repository.TenantChargeScheduleRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/masters/charge-schedules")
public class TenantChargeScheduleController {
    private final TenantChargeScheduleRepository repo;
    public TenantChargeScheduleController(TenantChargeScheduleRepository repo){this.repo=repo;}
    @GetMapping public List<TenantChargeSchedule> all(){return repo.findAll();}
    @GetMapping("/{id}") public TenantChargeSchedule get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
    @PostMapping public ResponseEntity<TenantChargeSchedule> create(@RequestBody TenantChargeSchedule x){return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(x));}
    @PutMapping("/{id}") public TenantChargeSchedule update(@PathVariable Long id,@RequestBody TenantChargeSchedule x){x.getClass(); var old=repo.findById(id).orElseThrow(); return repo.save(copyId(x,old));}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){repo.deleteById(id); return ResponseEntity.noContent().build();}
    private TenantChargeSchedule copyId(TenantChargeSchedule x,TenantChargeSchedule old) {
        try { var f=com.tubasmart.backend.model.BaseEntity.class.getDeclaredField("id"); f.setAccessible(true); f.set(x, f.get(old)); } catch(Exception e){throw new RuntimeException(e);}
        return x;
    }
}
