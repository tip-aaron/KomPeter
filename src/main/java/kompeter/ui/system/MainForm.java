/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.system;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import kompeter.ui.components.icons.SVGIconUIColor;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;

public class MainForm extends JPanel {
    private JButton buttonRedo;

    private JButton buttonRefresh;

    private JButton buttonUndo;

    private JPanel mainPanel;

    public MainForm() {
        init();
    }

    private JPanel createHeader() {
        final JPanel panel = new JPanel(new MigLayout("insets 3", "[]push[]push", "[fill]"));
        final JToolBar toolBar = new JToolBar();
        final JButton buttonDrawer = new JButton(new FlatSVGIcon(SVGIconUIColor.ICONS_BASE_PATH + "menu.svg", 0.75f));

        buttonUndo = new JButton(new FlatSVGIcon(SVGIconUIColor.ICONS_BASE_PATH + "undo.svg", 0.75f));
        buttonRedo = new JButton(new FlatSVGIcon(SVGIconUIColor.ICONS_BASE_PATH + "redo.svg", 0.75f));
        buttonRefresh = new JButton(new FlatSVGIcon(SVGIconUIColor.ICONS_BASE_PATH + "refresh.svg", 0.75f));

        buttonDrawer.addActionListener(e -> {
            if (Drawer.isOpen()) {
                Drawer.showDrawer();
            } else {
                Drawer.toggleMenuOpenMode();
            }
        });

        buttonUndo.addActionListener(e -> FormManager.undo());
        buttonRedo.addActionListener(e -> FormManager.redo());
        buttonRefresh.addActionListener(e -> FormManager.refresh());

        toolBar.add(buttonDrawer);
        toolBar.add(buttonUndo);
        toolBar.add(buttonRedo);
        toolBar.add(buttonRefresh);
        panel.add(toolBar);

        return panel;
    }

    private Component createMain() {
        mainPanel = new JPanel(new BorderLayout());
        return mainPanel;
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 0,gap 0", "[fill]", "[][fill,grow]"));
        add(createHeader(), "growx");
        add(createMain(), "grow");
    }

    public void refresh() {
    }

    public void setForm(final Form form) {
        buttonUndo.setEnabled(FormManager.FORMS.isUndoAble());
        buttonRedo.setEnabled(FormManager.FORMS.isRedoAble());

        if (mainPanel.getComponentOrientation().isLeftToRight() != form.getComponentOrientation().isLeftToRight()) {
            applyComponentOrientation(mainPanel.getComponentOrientation());
        }

        SwingUtilities.invokeLater(() -> {
            mainPanel.removeAll();
            mainPanel.add(form);
            mainPanel.repaint();
            mainPanel.revalidate();
        });
    }
}
