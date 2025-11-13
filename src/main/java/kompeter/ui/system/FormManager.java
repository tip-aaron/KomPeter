/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.system;

import javax.swing.JFrame;

import kompeter.services.auth.SessionManager;
import kompeter.ui.forms.FormProfile;
import kompeter.ui.forms.auth.FormAuthWelcome;
import kompeter.ui.menu.KompeterDrawerBuilder;
import kompeter.ui.utils.UndoRedo;
import kompeter.ui.utils.UndoRedo.RecentAction;
import raven.modal.Drawer;

public class FormManager {
    private static JFrame frame;
    private static MainAuthForm mainAuthForm;
    private static MainForm mainForm;
    private static FormAuthWelcome welcomeAuthForm;
    public static final UndoRedo<Form> AUTH_FORMS = new UndoRedo<>();
    public static final UndoRedo<Form> FORMS = new UndoRedo<>();

    private static MainAuthForm getMainAuthForm() {
        if (mainAuthForm == null) {
            mainAuthForm = new MainAuthForm();
        }

        return mainAuthForm;
    }

    private static MainForm getMainForm() {
        if (mainForm == null) {
            mainForm = new MainForm();
        }

        return mainForm;
    }

    private static FormAuthWelcome getWelcome() {
        if (welcomeAuthForm == null) {
            welcomeAuthForm = new FormAuthWelcome();
        }

        return welcomeAuthForm;
    }

    private static void install() {
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static void install(final JFrame f) {
        frame = f;
        install();

        if (SessionManager.getInstance().getSession() != null) {
            login();
        } else {
            logout();
        }
    }

    public static void login() {
        KompeterDrawerBuilder.getInstance().rebuildMenu();
        Drawer.setVisible(true);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(getMainForm());

        Drawer.setSelectedItemClass(FormProfile.class);

        AUTH_FORMS.clear();

        frame.repaint();
        frame.revalidate();
    }

    public static void logout() {
        Drawer.setVisible(false);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(getMainAuthForm());

        getMainForm().setForm(AllForms.getForm(FormProfile.class));
        getMainAuthForm().setForm(getWelcome());

        getWelcome().formInit();
        getWelcome().formCheck();
        getWelcome().formOpen();

        FORMS.clear();
        AllForms.clear();

        frame.repaint();
        frame.revalidate();
    }

    public static void redo() {
        if (FORMS.isRedoAble()) {
            final Form form = FORMS.redoDry().get();

            Drawer.setSelectedItemClass(form.getClass());
        }
    }

    public static void refresh() {
        if (!FORMS.current().isEmpty()) {
            FORMS.current().get().formRefresh();
            mainForm.refresh();
        }
    }

    public static void showAuthForm(final Form form) {
        if (AUTH_FORMS.current().isEmpty() || form != AUTH_FORMS.current().get()) {
            Form prevForm = null;

            if (AUTH_FORMS.current().isPresent()) {
                AUTH_FORMS.current().get().formBeforeClose();
                prevForm = AUTH_FORMS.current().get();
            }

            AUTH_FORMS.add(form);
            form.formCheck();
            form.formOpen();
            mainAuthForm.setForm(form);
            form.formAfterOpen();

            if (prevForm != null) {
                prevForm.formClose();
            }
        }
    }

    public static void showForm(final Form form) {
        Form prevForm = null;

        if (FORMS.current().isPresent()) {
            prevForm = FORMS.current().get();
        }

        if (FORMS.recentAction() == RecentAction.REDO || FORMS.recentAction() == RecentAction.UNDO) {
            if (FORMS.recentAction() == RecentAction.REDO) {
                FORMS.redo();
            } else if (FORMS.recentAction() == RecentAction.UNDO) {
                FORMS.undo();
            }

            FORMS.setRecentAction(null);
            form.formCheck();
            form.formOpen();
            mainForm.setForm(form);
            form.formAfterOpen();

            if (prevForm != null) {
                prevForm.formClose();
            }

            return;
        }

        if (FORMS.current().isEmpty() || form != FORMS.current().get()) {
            FORMS.add(form);
            form.formCheck();
            form.formOpen();
            mainForm.setForm(form);
            mainForm.refresh();
            form.formRefresh();
            form.formAfterOpen();

            if (prevForm != null) {
                prevForm.formClose();
            }
        }
    }

    public static void undo() {
        if (FORMS.isUndoAble()) {
            final Form form = FORMS.undoDry().get();

            Drawer.setSelectedItemClass(form.getClass());
        }
    }

    private final boolean isLoggedOut = true;

    public boolean isLoggedOut() {
        return isLoggedOut;
    }
}
