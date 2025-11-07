/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.system;

import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Form extends JPanel {
    private LookAndFeel oldTheme = UIManager.getLookAndFeel();

    public void formAfterOpen() {
    }

    public boolean formBeforeClose() {
        return true;
    }

    public boolean formBeforeLogout() {
        return true;
    }

    public final void formCheck() {
        if (oldTheme != UIManager.getLookAndFeel()) {
            oldTheme = UIManager.getLookAndFeel();
            SwingUtilities.updateComponentTreeUI(this);
        }
    }

    /** Cleanup listeners */
    public void formClose() {
    }

    /** Cleanup everything */
    public void formDestroy() {
    }

    public void formInit() {
    }

    /** Reinitialize listeners */
    public void formOpen() {
    }

    /** Reload data */
    public void formRefresh() {
    }

    @FunctionalInterface
    public interface FormBeforeCloseCallback {
        void beforeClose(boolean predicate);
    }
}
