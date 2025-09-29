package com.github.ragudos.kompeter.database.dto;

import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record ItemCategoryAssignmentDto(
        int _itemCategoryAssignmentId,
        @NotNull Timestamp _createdAt,
        int _itemId,
        int _itemCategoryId) {}
