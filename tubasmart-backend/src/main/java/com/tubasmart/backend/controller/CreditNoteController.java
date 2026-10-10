package com.tubasmart.backend.controller;
import com.tubasmart.backend.model.*; import com.tubasmart.backend.repository.*; import com.tubasmart.backend.service.CreditNoteService; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/credit-notes") public class CreditNoteController {
 private final CreditNoteRepository repo; private final CreditNoteService service;
 public CreditNoteController(CreditNoteRepository r,CreditNoteService s){repo=r;service=s;}
 @GetMapping public List<CreditNote> all(){return repo.findAll();}
 @GetMapping("/{id}") public CreditNote get(@PathVariable Long id){return repo.findById(id).orElseThrow();}
 @PostMapping public ResponseEntity<CreditNote> create(@RequestBody CreditNoteService.CreateRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.issue(r));}
 @PostMapping("/{id}/cancel") public CreditNote cancel(@PathVariable Long id){CreditNote x=repo.findById(id).orElseThrow();x.setStatus(DocumentStatus.CANCELLED);return repo.save(x);}
 @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){throw new IllegalStateException("Credit notes must be cancelled; they cannot be deleted");}
}
