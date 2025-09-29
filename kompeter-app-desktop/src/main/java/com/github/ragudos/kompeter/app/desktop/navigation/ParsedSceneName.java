package com.github.ragudos.kompeter.app.desktop.navigation;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;

public class ParsedSceneName implements Iterator<ParsedSceneName> {
    private @NotNull final String fullPath;
    private @NotNull final String current;
    private final ParsedSceneName parent;
    private final ParsedSceneName next;

    private ParsedSceneName cursor = this;

    public ParsedSceneName(
            @NotNull String fullPath,
            @NotNull String current,
            ParsedSceneName parent,
            ParsedSceneName next) {
        this.fullPath = fullPath;
        this.current = current;
        this.parent = parent;
        this.next = next;
    }

    public static final String SEPARATOR = "/";

    public static @NotNull ParsedSceneName parse(@NotNull final String sceneName) {
        if (sceneName.isEmpty()) {
            return new ParsedSceneName(sceneName, "", null, null);
        }

        int index = sceneName.indexOf(SEPARATOR);

        if (index == -1) {
            return new ParsedSceneName(sceneName, sceneName, null, null);
        }

        String current = sceneName.substring(0, index);
        String rest = sceneName.substring(index + 1);

        ParsedSceneName node = new ParsedSceneName(sceneName, current, null, null);
        ParsedSceneName child = build(sceneName, rest, node);

        return new ParsedSceneName(sceneName, current, null, child);
    }

    private static ParsedSceneName build(String fullPath, String remaining, ParsedSceneName parent) {
        int index = remaining.indexOf(SEPARATOR);

        if (index == -1) {
            return new ParsedSceneName(fullPath, remaining, parent, null);
        }

        String current = remaining.substring(0, index);
        String rest = remaining.substring(index + 1);

        ParsedSceneName node = new ParsedSceneName(remaining, current, parent, null);
        ParsedSceneName child = build(fullPath, rest, node);

        return new ParsedSceneName(fullPath, current, parent, child);
    }

    @Override
    public boolean hasNext() {
        return cursor != null;
    }

    @Override
    public ParsedSceneName next() {
        if (cursor == null) {
            throw new NoSuchElementException();
        }

        ParsedSceneName res = cursor;

        cursor = cursor.nextNode();

        return res;
    }

    public @NotNull String fullPath() {
        return this.fullPath;
    }

    public @NotNull String current() {
        return this.current;
    }

    public ParsedSceneName parent() {
        return parent;
    }

    public ParsedSceneName nextNode() {
        return next;
    }
}
