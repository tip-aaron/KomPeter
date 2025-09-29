package com.github.ragudos.kompeter.database.dto;

import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record PurchaseDto(
        int _purchaseId,
        int _supplierId,
        @NotNull Timestamp _createdAt,
        @NotNull Timestamp purchaseDate,
        @NotNull String purchaseCode,
        Timestamp deliveryDate,
        @NotNull BigDecimal vatPercent,
        BigDecimal discountValue,
        DiscountType discountType) {}
