package kompeter.ui.frames;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JWindow;

import kompeter.loader.AssetLoader;
import kompeter.ui.components.panels.ImagePanel;
import kompeter.ui.components.panels.ImagePanel.ScaleMode;
import net.miginfocom.swing.MigLayout;

public class SplashScreen extends JWindow {
    ImagePanel imagePanel;
    JLabel text;

    public SplashScreen() {
        imagePanel = new ImagePanel(AssetLoader.loadImage("logo.png", false), false);
        text = new JLabel("Loading...");

        setLayout(new MigLayout("insets 12, flowx, wrap, gap 12px, al center center", "[grow, center, fill]"));

        imagePanel.setScaleMode(ScaleMode.CONTAIN);

        setMinimumSize(new Dimension(200, 150));
        setPreferredSize(new Dimension(200, 150));

        imagePanel.setMinimumSize(new Dimension(100, 100));
        imagePanel.setMaximumSize(new Dimension(100, 100));

        text.setHorizontalAlignment(JLabel.CENTER);

        add(imagePanel, "grow");
        add(text, "grow");

        setVisible(true);
    }
}
