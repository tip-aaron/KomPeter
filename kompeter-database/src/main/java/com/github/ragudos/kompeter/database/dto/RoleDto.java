package com.github.ragudos.kompeter.database.dto;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

public record RoleDto(int _roleId, @NotNull Timestamp _createdAt, @NotNull String roleName, String description) {
}
