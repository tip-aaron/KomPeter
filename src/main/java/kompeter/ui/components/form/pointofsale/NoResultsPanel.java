/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.components.form.pointofsale;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class NoResultsPanel extends JPanel {
    public NoResultsPanel() {
        setLayout(new MigLayout("insets 6", "[grow, fill, center]"));

        final JLabel label = new JLabel("No results found :(");

        label.setHorizontalAlignment(JLabel.CENTER);

        add(label, "growx");
    }
}
