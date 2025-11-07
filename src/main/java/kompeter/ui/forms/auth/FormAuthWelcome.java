/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.forms.auth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.ui.system.Form;
import kompeter.ui.system.FormManager;
import net.miginfocom.swing.MigLayout;

public class FormAuthWelcome extends Form {
    private JButton signInButton;
    private SignInButtonActionListener signInButtonActionListener;

    private JButton signUpButton;
    private SignUpButtonActionListener signUpButtonActionListener;

    public FormAuthWelcome() {
        init();
    }

    private void createWelcome() {
        final JPanel titleContainer = new JPanel(new MigLayout("gapx 9px"));
        final JLabel title = new JLabel("KomPeter");
        final JLabel subtitle = new JLabel("<html>Computer Parts<br>& Accessories</html>");

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h00 primary");
        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1 primary");

        titleContainer.add(title);
        titleContainer.add(subtitle);

        final JPanel buttonContainer = new JPanel(new MigLayout("gapx 48px, al center center"));
        signInButton = new JButton("Sign in");
        signUpButton = new JButton("Sign up");

        signInButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        signInButton.putClientProperty(FlatClientProperties.BUTTON_TYPE_ROUND_RECT, true);
        signUpButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        signUpButton.putClientProperty(FlatClientProperties.BUTTON_TYPE_ROUND_RECT, true);

        buttonContainer.add(signInButton);
        buttonContainer.add(signUpButton);

        add(titleContainer, "growx");
        add(buttonContainer, "growx");
    }

    private void init() {
        setLayout(new MigLayout("flowy, gapy 18px, al center center"));

        signInButtonActionListener = new SignInButtonActionListener();
        signUpButtonActionListener = new SignUpButtonActionListener();

        createWelcome();
    }

    @Override
    public void formClose() {
        signInButton.removeActionListener(signInButtonActionListener);
        signUpButton.removeActionListener(signUpButtonActionListener);
    }

    @Override
    public void formOpen() {
        signInButton.addActionListener(signInButtonActionListener);
        signUpButton.addActionListener(signUpButtonActionListener);
    }

    private class SignInButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            FormManager.showAuthForm(new FormAuthLogin());
        }
    }

    private class SignUpButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            FormManager.showAuthForm(new FormAuthRegister());
        }
    }
}
