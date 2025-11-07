/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.components.tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;

import lombok.Builder;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class ItemStatusText extends JPanel implements TableCellRenderer {
    private Color bg;
    private Color fg;
    JLabel label;

    public ItemStatusText() {
        label = new JLabel();
        bg = getBackground();
        fg = getForeground();

        setMinimumSize(new Dimension(68, getMinimumSize().height));

        setAlignmentX(0.5f);
        setAlignmentY(0.5f);

        setOpaque(false);

        label.putClientProperty(FlatClientProperties.STYLE, "arc: 999;border: 6,12,6,12; font: 11 bold;");

        setLayout(new MigLayout("insets 6, al center center"));

        add(label, "");
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        final ItemStatus itemStatus = (ItemStatus) value;

        if (itemStatus == null) {
            label.setText("");
        } else {
            if (itemStatus.isDeleted) {
                label.setText("archived");
            } else if (itemStatus.isActive) {
                label.setText("active");
            } else {
                label.setText("inactive");
            }
        }

        if (isSelected) {
            setOpaque(true);
            bg = getBackground();
            fg = getForeground();
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setOpaque(false);
            setBackground(bg);
            setForeground(fg);
        }

        if (itemStatus.isDeleted) {
            label.setBackground(Color.decode("#FF6961"));
            label.setForeground(Color.decode("#ffccca"));
        } else if (itemStatus.isActive) {
            label.setBackground(Color.decode("#80EF80"));
            label.setForeground(Color.decode("#084108"));
        } else {
            label.setBackground(Color.decode("#c2bdb9"));
            label.setForeground(Color.decode("#282623"));
        }

        return this;
    }

    @Getter
    @Builder
    public static class ItemStatus {
        boolean isActive;
        boolean isDeleted;
    }
}
