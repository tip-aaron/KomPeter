package com.github.ragudos.kompeter.app.desktop.scenes.home.monitoring.scenes;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public final class MonitoringOverviewScene implements Scene {
    public static final String SCENE_NAME = "overview";

    private final JPanel view = new JPanel();

    public MonitoringOverviewScene() {
        onCreate();
    }

    @Override
    public @NotNull String name() {
        return SCENE_NAME;
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }

    @Override
    public void onCreate() {
        view.setLayout(new MigLayout("insets 20, fill", "[grow,center]", "[grow,center]"));

        JLabel titleLabel = new JLabel("Monitoring Overview");
        titleLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1");

        view.add(titleLabel, "wrap");
    }
}
