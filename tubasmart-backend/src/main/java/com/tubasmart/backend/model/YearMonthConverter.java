package com.tubasmart.backend.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.YearMonth;

/** Stores YearMonth as 'yyyy-MM' text so it matches the varchar billing_period column. */
@Converter(autoApply = true)
public class YearMonthConverter implements AttributeConverter<YearMonth, String> {
    @Override public String convertToDatabaseColumn(YearMonth v) { return v == null ? null : v.toString(); }
    @Override public YearMonth convertToEntityAttribute(String v) { return v == null || v.isBlank() ? null : YearMonth.parse(v); }
}
