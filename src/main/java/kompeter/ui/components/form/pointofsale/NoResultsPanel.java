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
    public final JLabel label;

    public NoResultsPanel() {
        setLayout(new MigLayout("insets 0", "[grow, fill, center]"));

        label = new JLabel("No results found :(");

        add(label, "growx");
    }
}
