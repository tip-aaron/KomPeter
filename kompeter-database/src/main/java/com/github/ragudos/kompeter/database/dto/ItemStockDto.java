package com.github.ragudos.kompeter.database.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record ItemStockDto(
        int _itemStockId,
        @NotNull Timestamp _createdAt,
        int _itemId,
        int _itemBrandId,
        @NotNull BigDecimal unitPricePhp,
        int quantity,
        int minimumQuantity) {}
