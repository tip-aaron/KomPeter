package com.github.ragudos.kompeter.app.desktop.navigation;

import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public interface SceneManager {
    void destroy();

    void destroyScene(@NotNull Scene scene);

    @NotNull Scene currentScene();

    @NotNull String currentSceneName();

    @NotNull JPanel view();

    @NotNull Scene scene(@NotNull final String name);

    boolean navigateTo(@NotNull final String name);

    void registerScene(@NotNull final String name, @NotNull final SceneFactory factory);

    void registerScene(
            @NotNull final String name,
            @NotNull final SceneFactory factory,
            @NotNull final SceneGuard guard);

    void unregisterScene(@NotNull final String name);
}
