package com.github.ragudos.kompeter.app.desktop.components;

import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public class MainHeader implements SceneComponent {
    private static final Logger LOGGER = KompeterLogger.getLogger(MainHeader.class);

    private final JPanel view;

    public MainHeader() {
        view = new JPanel();
    }

    @Override
    public void destroy() {}

    @Override
    public void initialize() {}

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }
}
