package com.tubasmart.backend.controller;

import com.tubasmart.backend.repository.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;

@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
    private final InvoiceRepository invoices; private final ReceiptRepository receipts;
    private final CustomerRepository customers; private final FloorRepository floors;
    public DashboardController(InvoiceRepository i, ReceiptRepository r, CustomerRepository c, FloorRepository f){
        invoices=i; receipts=r; customers=c; floors=f;
    }
    @GetMapping public Map<String,Object> dashboard(){
        BigDecimal billed=invoices.findAll().stream().filter(x->x.getStatus()==com.tubasmart.backend.model.DocumentStatus.ISSUED).map(x->x.getTotal()==null?BigDecimal.ZERO:x.getTotal()).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal collected=receipts.findAll().stream().filter(x->x.getStatus()!=com.tubasmart.backend.model.DocumentStatus.CANCELLED).map(x->x.getAmount()==null?BigDecimal.ZERO:x.getAmount()).reduce(BigDecimal.ZERO,BigDecimal::add);
        long occupied=floors.findAll().stream().filter(x->x.getStatus()!=null && x.getStatus().name().equals("OCCUPIED")).count();
        long total=floors.count();
        return Map.of("billed",billed,"collected",collected,"outstanding",billed.subtract(collected),
                "customers",customers.count(),"occupiedFloors",occupied,"totalFloors",total,
                "occupancyPercent", total==0?0:(occupied*100.0/total));
    }
}
