package com.github.ragudos.kompeter.app.desktop.navigation;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface SceneFactory {
    @NotNull Scene createScene();
}
