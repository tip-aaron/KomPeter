/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.system;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class MainAuthForm extends JPanel {
    public MainAuthForm() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
    }

    public void setForm(Form form) {
        removeAll();
        add(form, BorderLayout.CENTER);
        repaint();
        revalidate();
    }
}
