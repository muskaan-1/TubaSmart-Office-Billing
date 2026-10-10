package com.tubasmart.backend.controller;

import com.tubasmart.backend.model.Invoice;
import com.tubasmart.backend.service.BillingService;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.YearMonth;

@RestController @RequestMapping("/api/billing")
public class BillingController {
    private final BillingService service;
    public BillingController(BillingService service){this.service=service;}
    public record GenerateRequest(@NotNull Long tenantId,@NotNull String billingPeriod,@NotNull @PositiveOrZero BigDecimal taxableAmount,@NotNull @PositiveOrZero BigDecimal gstRate,String supplyState,String companyState) {}
    public record PreviewRequest(@NotNull Long tenantId,@NotNull String billingPeriod,String supplyState,String companyState) {}
    public record RunRequest(@NotNull Long tenantId,@NotNull String billingPeriod,String supplyState,String companyState) {}
    @PostMapping("/runs/preview") public BillingService.BillingPreview preview(@RequestBody PreviewRequest r){return service.preview(r.tenantId(),YearMonth.parse(r.billingPeriod()),r.supplyState(),r.companyState());}
    @PostMapping("/runs/generate") public Invoice generate(@RequestBody GenerateRequest r){return service.generate(r.tenantId(),YearMonth.parse(r.billingPeriod()),r.taxableAmount(),r.gstRate(),r.supplyState(),r.companyState());}
    @PostMapping("/runs/generate-from-prices") public Invoice generateFromPrices(@RequestBody RunRequest r){return service.generateFromPreview(r.tenantId(),YearMonth.parse(r.billingPeriod()),r.supplyState(),r.companyState());}
}
