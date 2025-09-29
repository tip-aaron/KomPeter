package com.github.ragudos.kompeter.database.dto;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

public record SessionDto(int _sessionId, @NotNull Timestamp _createdAt, int _userId,
        @NotNull String sessionToken, String ipAddress) {
}
