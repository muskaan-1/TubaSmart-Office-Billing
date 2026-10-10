package com.tubasmart.backend;

import com.tubasmart.backend.model.YearMonthConverter;
import org.junit.jupiter.api.Test;
import java.time.YearMonth;
import static org.junit.jupiter.api.Assertions.*;

class YearMonthConverterTest {
    private final YearMonthConverter c = new YearMonthConverter();
    @Test void roundTrips() {
        assertEquals("2026-04", c.convertToDatabaseColumn(YearMonth.of(2026, 4)));
        assertEquals(YearMonth.of(2026, 4), c.convertToEntityAttribute("2026-04"));
    }
    @Test void handlesNulls() {
        assertNull(c.convertToDatabaseColumn(null));
        assertNull(c.convertToEntityAttribute(null));
        assertNull(c.convertToEntityAttribute(" "));
    }
}
