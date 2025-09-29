package com.github.ragudos.kompeter.database.dto;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

public record ItemBrandDto(int _itemBrandId, @NotNull Timestamp _createdAt, @NotNull String name, String description) {
}
