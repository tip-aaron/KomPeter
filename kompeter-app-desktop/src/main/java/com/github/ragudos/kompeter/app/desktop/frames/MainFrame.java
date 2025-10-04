package com.github.ragudos.kompeter.app.desktop.frames;

import com.github.ragudos.kompeter.utilities.constants.Metadata;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import java.awt.Dimension;

public class MainFrame extends JFrame {
    private class MainFrameWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            // TODO: Cleanup operations
            dispose();
        }
    }

    private final MainFrameWindowListener windowListener = new MainFrameWindowListener();

    public MainFrame() {
        addWindowListener(windowListener);

        setTitle(Metadata.APP_TITLE + " " + Metadata.APP_VERSION);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setSize(new Dimension(400,400));
        setLocationRelativeTo(null);
    }
}
