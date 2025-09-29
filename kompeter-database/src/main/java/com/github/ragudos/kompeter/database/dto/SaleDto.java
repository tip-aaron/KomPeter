package com.github.ragudos.kompeter.database.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.database.dto.enums.DiscountType;

public record SaleDto(int _saleId, @NotNull Timestamp _createdAt, @NotNull Timestamp saleDate, @NotNull String saleCode,
        String customerName, @NotNull BigDecimal vatPercent, BigDecimal discountValue, DiscountType discountType) {
}
