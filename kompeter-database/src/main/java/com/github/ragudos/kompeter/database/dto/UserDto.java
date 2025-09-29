package com.github.ragudos.kompeter.database.dto;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

public record UserDto(int _userId, @NotNull Timestamp _createdAt, @NotNull String displayName,
        @NotNull String firstName, @NotNull String lastName) {
}
