/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.frames;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.constants.Metadata;
import kompeter.ui.menu.KompeterDrawerBuilder;
import kompeter.ui.system.FormManager;
import raven.modal.Drawer;

public class MainFrame extends JFrame {
    WindowAdapter windowListener;

    public MainFrame() {
        Drawer.installDrawer(rootPane, KompeterDrawerBuilder.getInstance());
        FormManager.install(this);

        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);

        setTitle(String.format("%s v%s", Metadata.APP_TITLE, Metadata.APP_VERSION));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(1280, 720));
        setSize(getPreferredSize());
        setLocationRelativeTo(null);

        windowListener = new MainFrameWindowAdapter();

        addWindowListener(windowListener);
    }

    class MainFrameWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(final WindowEvent e) {
            dispose();
            // helps removes warning when using SwingWorker
            System.exit(0);
        }
    }
}
