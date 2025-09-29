package com.github.ragudos.kompeter.app.desktop.navigation;

import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public interface Scene {
    default boolean canHide() {
        return true;
    }

    default boolean canShow() {
        return true;
    }

    default @NotNull Scene self() {
        return this;
    }

    @NotNull String name();

    @NotNull JPanel view();

    default void onBeforeHide() {}

    default void onBeforeShow() {}

    default void onCannotHide() {}

    default void onCannotShow() {}

    void onCreate();

    void onDestroy();

    void onHide();

    void onShow();

    default boolean supportsSubScenes() {
        return false;
    }
}
