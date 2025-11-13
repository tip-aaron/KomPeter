package kompeter.ui.components.textfields;

import java.util.function.Consumer;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.ui.components.icons.SVGIconUIColor;

public class SearchTextField extends JTextField implements DocumentListener {
    private final Consumer<String> onSearch;

    public SearchTextField(final Consumer<String> onSearch) {
        putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new SVGIconUIColor("search.svg", 0.75f, "TextField.placeholderForeground"));

        this.onSearch = onSearch;
    }

    public void addListeners() {
        putClientProperty(FlatClientProperties.TEXT_FIELD_CLEAR_CALLBACK, (Consumer<JTextField>) (jtf) -> {
            onSearch.accept("");
            jtf.setText("");
        });
        getDocument().addDocumentListener(this);
    }

    public void removeListeners() {
        putClientProperty(FlatClientProperties.TEXT_FIELD_CLEAR_CALLBACK, null);
        getDocument().removeDocumentListener(this);
    }

    @Override
    public void changedUpdate(final DocumentEvent e) {

    }

    @Override
    public void insertUpdate(final DocumentEvent e) {
        onSearch.accept(getText());
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
        onSearch.accept(getText());
    }
}
