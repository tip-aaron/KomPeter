package com.github.ragudos.kompeter.cryptography;

import com.github.ragudos.kompeter.utilities.CharUtils;
import org.jetbrains.annotations.NotNull;

public final record HashedStringWithSalt(@NotNull byte[] hashedString, @NotNull Salt salt) {
    public boolean equalsHashedString(@NotNull final HashedStringWithSalt hsws) {
        return CharUtils.constantTimeEquals(hsws.hashedString(), this.hashedString());
    }

    public boolean equalsSalt(@NotNull final HashedStringWithSalt hsws) {
        return CharUtils.constantTimeEquals(hsws.salt().value(), this.salt().value());
    }

    public boolean equals(@NotNull final HashedStringWithSalt hsws) {
        return equalsHashedString(hsws) && equalsSalt(hsws);
    }

    public boolean isHashedStringEmpty() {
        int zeroCount = 0;

        for (int i = 0; i < hashedString().length; ++i) {
            if (hashedString()[i] == 0) {
                zeroCount++;
            }
        }

        return zeroCount == hashedString().length;
    }

    public void clearHashedStringBytes() {
        for (int i = 0; i < hashedString().length; ++i) {
            hashedString()[i] = 0;
        }
    }
}
