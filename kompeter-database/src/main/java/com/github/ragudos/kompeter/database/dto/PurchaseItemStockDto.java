package com.github.ragudos.kompeter.database.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

public record PurchaseItemStockDto(int _purchaseItemStockId, int _purchaseId, int _itemStockId,
        @NotNull Timestamp _createdAt, int quantityOrdered, int quantityReceived, @NotNull BigDecimal unitCostPhp) {
}
