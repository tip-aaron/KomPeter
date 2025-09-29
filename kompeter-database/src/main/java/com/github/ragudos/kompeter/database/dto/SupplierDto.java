package com.github.ragudos.kompeter.database.dto;

import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record SupplierDto(
        int _supplierId,
        @NotNull Timestamp _createdAt,
        @NotNull String name,
        @NotNull String email,
        String street,
        String city,
        String state,
        String postalCode,
        String country) {}
