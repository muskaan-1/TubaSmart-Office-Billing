package com.tubasmart.backend;

import org.junit.jupiter.api.Test;
import java.math.*;
import static org.junit.jupiter.api.Assertions.*;

class BillingRulesTest {
    private static BigDecimal money(BigDecimal x){return x.setScale(2,RoundingMode.HALF_UP);}
    @Test void sameStateTaxSplitsEvenly(){
        BigDecimal taxable=new BigDecimal("10000.00"); BigDecimal tax=money(taxable.multiply(new BigDecimal("18")).divide(new BigDecimal("100"),2,RoundingMode.HALF_UP));
        BigDecimal cgst=money(tax.divide(new BigDecimal("2"),2,RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("900.00"),cgst); assertEquals(new BigDecimal("900.00"),tax.subtract(cgst));
    }
    @Test void tdsReducesNetPayableButNotGross(){
        BigDecimal taxable=new BigDecimal("10000.00"), gst=new BigDecimal("1800.00"), gross=taxable.add(gst);
        BigDecimal tds=money(taxable.multiply(new BigDecimal("10")).divide(new BigDecimal("100"),2,RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("11800.00"),gross); assertEquals(new BigDecimal("10800.00"),gross.subtract(tds));
    }
}
