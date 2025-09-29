package com.github.ragudos.kompeter.database.dto;

import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record ItemRestockDto(
        int _itemRestockId,
        @NotNull Timestamp _createdAt,
        int _itemStockId,
        int quantityBefore,
        int quantityAfter,
        int quantityAdded) {}
