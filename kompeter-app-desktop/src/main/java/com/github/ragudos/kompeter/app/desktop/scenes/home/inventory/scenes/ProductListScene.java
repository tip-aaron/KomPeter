package com.github.ragudos.kompeter.app.desktop.scenes.home.inventory.scenes;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public final class ProductListScene implements Scene {
    public static final String SCENE_NAME = "product_list";

    private final JPanel view = new JPanel();

    public ProductListScene() {
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

        JLabel titleLabel = new JLabel("Product List");
        titleLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1");

        view.add(titleLabel, "wrap");
    }
}
