/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;

import net.miginfocom.swing.MigLayout;

public class MainHeader implements SceneComponent {
    private final AtomicBoolean initialized;

    private final Consumer<String> navigationListenerClassConsumer = new Consumer<String>() {
        @Override
        public void accept(String sceneName) {
            String[] splitName = sceneName.split(ParsedSceneName.SEPARATOR);

            if (splitName.length <= 1) {
                return;
            }

            refreshLine.refresh();
            sceneTitle.setText(SceneNames.toReadable(splitName[1]));
        }
    };

    private final RefreshLine refreshLine;
    private final JLabel sceneTitle;

    private final JPanel view;

    public MainHeader() {
        initialized = new AtomicBoolean(false);
        refreshLine = new RefreshLine();
        sceneTitle = new JLabel();
        view = new JPanel(new MigLayout("insets n 16 n 16, flowy", "[grow, fill]", "[grow]"));
        sceneTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1");
        sceneTitle.setHorizontalAlignment(JLabel.RIGHT);
    }

    @Override
    public void destroy() {
        if (!initialized.get()) {
            return;
        }

        SceneNavigator.getInstance().unsubscribe(navigationListenerClassConsumer);
        view.removeAll();
        sceneTitle.setText("");
        initialized.set(false);
    }

    @Override
    public void initialize() {
        if (initialized.get()) {
            return;
        }

        JPanel container = new JPanel(new MigLayout("insets n 16 n 16", "[]push[]", "[grow, center]"));

        view.putClientProperty(FlatClientProperties.STYLE, "background:tint($Panel.background, 5%);");
        container.putClientProperty(FlatClientProperties.STYLE, "background:tint($Panel.background, 5%);");

        container.add(sceneTitle);

        view.add(container, "grow");
        view.add(refreshLine, "grow, height 2!");

        SceneNavigator.getInstance().subscribe(navigationListenerClassConsumer);
        initialized.set(true);
    }

    @Override
    public boolean isInitialized() {
        return initialized.get();
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }
}
