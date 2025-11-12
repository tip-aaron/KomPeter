/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.forms.auth;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.CharBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.constants.StringLimits;
import kompeter.constants.StringPatterns;
import kompeter.services.auth.Authentication;
import kompeter.services.auth.AuthenticationStatus;
import kompeter.ui.components.icons.SVGIconUIColor;
import kompeter.ui.system.Form;
import kompeter.ui.system.FormManager;
import kompeter.ui.utils.HtmlUtils;
import net.miginfocom.swing.MigLayout;
import raven.extras.SlidePane;
import raven.extras.SlidePaneTransition;
import raven.modal.component.DropShadowBorder;
import raven.modal.slider.PanelSlider.SliderCallback;

public class FormAuthRegister extends Form {
    private Icon cachedRegisterButtonIcon;
    private JPanel contentContainer;

    private final AtomicInteger currentStep;
    private JLabel displayNameError;
    private JLabel displayNameLabel;
    private JTextField displayNameTextField;

    private JLabel emailError;

    private JLabel emailLabel;
    private JTextField emailTextField;
    private EnterKeyListener enterKeyListener;
    private JLabel firstNameError;
    private JLabel firstNameLabel;
    private JTextField firstNameTextField;
    private final AtomicBoolean isBusy;
    private JLabel lastNameError;
    private JLabel lastNameLabel;
    private JTextField lastNameTextField;
    private JButton loginButton;
    private LoginButtonActionListener loginButtonActionListener;
    private SlidePane paneSlider;
    private JLabel passwordError;
    private JLabel passwordLabel;
    private JPasswordField passwordTextField;
    private JButton previousStepButton;
    private PreviousStepButtonListener previousStepButtonListener;
    private JButton registerButton;
    private RegisterButtonActionListener registerButtonActionListener;
    private JPanel step1Panel;
    private JPanel step2Panel;
    private JPanel step3Panel;
    private JLabel stepLabel;

    public FormAuthRegister() {
        currentStep = new AtomicInteger(1);
        isBusy = new AtomicBoolean(false);

        init();
    }

    private void applyShadowBorder(final JPanel panel) {
        if (panel != null) {
            panel.setBorder(new DropShadowBorder(new Insets(4, 8, 12, 8), 1, 25));
        }
    }

    private void clearStep1Errors() {
        firstNameTextField.putClientProperty("JComponent.outline", null);
        lastNameTextField.putClientProperty("JComponent.outline", null);
        firstNameError.setText("");
        lastNameError.setText("");
    }

    private void clearStep2Errors() {
        displayNameTextField.putClientProperty("JComponent.outline", null);
        emailTextField.putClientProperty("JComponent.outline", null);
        displayNameError.setText("");
        emailError.setText("");
    }

    private void clearStep3Errors() {
        passwordTextField.putClientProperty("JComponent.outline", null);
        passwordError.setText("");
    }

    private void createRegister() {
        paneSlider = new SlidePane();

        final JPanel container = new JPanel(new BorderLayout()) {
            @Override
            public void updateUI() {
                super.updateUI();
                applyShadowBorder(this);
            }
        };

        container.setOpaque(false);
        applyShadowBorder(container);

        contentContainer = new JPanel(new MigLayout("fillx, wrap, insets 35 35 25 35", "[fill, 300]"));

        final JLabel title = new JLabel("Welcome!");
        final JLabel description = new JLabel("Sign up to create an account.");

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2 primary");
        description.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 muted");

        contentContainer.add(title);
        contentContainer.add(description);

        stepLabel = new JLabel(String.format("Step: %s", currentStep.get()));

        step1Panel = new JPanel(new MigLayout("fillx, wrap, insets 0", "[fill, 300]"));
        step2Panel = new JPanel(new MigLayout("fillx, wrap, insets 0", "[fill, 300]"));
        step3Panel = new JPanel(new MigLayout("fillx, wrap, insets 0", "[fill, 300]"));

        firstNameLabel = new JLabel("First Name*");
        firstNameTextField = new JTextField();
        firstNameError = new JLabel();
        lastNameLabel = new JLabel("Last Name*");
        lastNameTextField = new JTextField();
        lastNameError = new JLabel();
        displayNameLabel = new JLabel("Display Name*");
        displayNameTextField = new JTextField();
        displayNameError = new JLabel();
        emailLabel = new JLabel("Email*");
        emailTextField = new JTextField();
        emailError = new JLabel();
        passwordLabel = new JLabel("Password*");
        passwordTextField = new JPasswordField();
        passwordError = new JLabel();
        previousStepButton = new JButton("Back", new SVGIconUIColor("move-left.svg", 1f, "foreground.muted"));
        registerButton = new JButton("Continue", new SVGIconUIColor("move-right.svg", 1f, "foreground.primary"));
        loginButton = new JButton("I already have an account");

        stepLabel.putClientProperty(FlatClientProperties.STYLE, "font: +1 semibold;");

        firstNameTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your first name");
        lastNameTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your last name");
        displayNameTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your display name");
        emailTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email");
        passwordTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");

        firstNameError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
        lastNameError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
        displayNameError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
        emailError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
        passwordError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");

        firstNameError.putClientProperty(FlatClientProperties.STYLE, "font:-3;");
        lastNameError.putClientProperty(FlatClientProperties.STYLE, "font:-3;");
        displayNameError.putClientProperty(FlatClientProperties.STYLE, "font:-3;");
        emailError.putClientProperty(FlatClientProperties.STYLE, "font:-3;");
        passwordError.putClientProperty(FlatClientProperties.STYLE, "font:-3;");

        previousStepButton.setIconTextGap(16);
        previousStepButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        previousStepButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
        registerButton.setIconTextGap(16);
        registerButton.setHorizontalTextPosition(SwingConstants.LEFT);
        registerButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        loginButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "link");

        container.putClientProperty(FlatClientProperties.STYLE, "[dark]:tint($Panel.background, 1%);");
        contentContainer.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        paneSlider.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        step1Panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        step2Panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        step3Panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");

        contentContainer.add(stepLabel, "gapy 25");
        contentContainer.add(paneSlider, "width 100%, gapy 10");

        paneSlider.addSlide(step1Panel);

        step1Panel.add(firstNameLabel);
        step1Panel.add(firstNameTextField);
        step1Panel.add(firstNameError, "gapy 2");

        step1Panel.add(lastNameLabel, "gapy 5");
        step1Panel.add(lastNameTextField);
        step1Panel.add(lastNameError, "gapy 2");

        step2Panel.add(displayNameLabel);
        step2Panel.add(displayNameTextField);
        step2Panel.add(displayNameError, "gapy 2");

        step2Panel.add(emailLabel, "gapy 5");
        step2Panel.add(emailTextField);
        step2Panel.add(emailError, "gapy 2");

        step3Panel.add(passwordLabel);
        step3Panel.add(passwordTextField);
        step3Panel.add(passwordError, "gapy 2");

        contentContainer.add(registerButton, "gapy 20");
        contentContainer.add(loginButton, "gapy 35");

        container.add(contentContainer);
        add(container);
    }

    private void init() {
        setLayout(new MigLayout("al center center"));

        enterKeyListener = new EnterKeyListener();
        previousStepButtonListener = new PreviousStepButtonListener(this);
        loginButtonActionListener = new LoginButtonActionListener();
        registerButtonActionListener = new RegisterButtonActionListener(this);

        createRegister();
    }

    private boolean validateDisplayName() {
        final String displayName = displayNameTextField.getText();

        if (displayName.length() < StringLimits.DISPLAY_NAME.min()
                || displayName.length() > StringLimits.DISPLAY_NAME.max()) {
            displayNameTextField.putClientProperty("JComponent.outline", "error");
            displayNameError.setText(
                    String.format(HtmlUtils.wrapInHtml(HtmlUtils.escapeHtml("Display name must be >= %d and <= %d")),
                            StringLimits.DISPLAY_NAME.min(), StringLimits.DISPLAY_NAME.max()));

            return false;
        }

        return true;
    }

    private boolean validateEmail() {
        final String email = emailTextField.getText();

        if (!StringPatterns.EMAIL_REGEX.matcher(email).find()) {
            emailTextField.putClientProperty("JComponent.outline", "error");
            emailError.setText("Invalid email");

            return false;
        }

        return true;
    }

    private boolean validateFirstName() {
        final String firstName = firstNameTextField.getText();

        if (firstName.length() < StringLimits.FIRST_NAME.min() || firstName.length() > StringLimits.FIRST_NAME.max()) {
            firstNameTextField.putClientProperty("JComponent.outline", "error");
            firstNameError.setText(
                    String.format(HtmlUtils.wrapInHtml(HtmlUtils.escapeHtml("First name must be >= %d and <= %d")),
                            StringLimits.FIRST_NAME.min(), StringLimits.FIRST_NAME.max()));

            return false;
        }

        return true;
    }

    private boolean validateLastName() {
        final String lastName = lastNameTextField.getText();

        if (lastName.length() < StringLimits.LAST_NAME.min() || lastName.length() > StringLimits.LAST_NAME.max()) {
            lastNameTextField.putClientProperty("JComponent.outline", "error");
            lastNameError.setText(
                    String.format(HtmlUtils.wrapInHtml(HtmlUtils.escapeHtml("Last name must be >= %d and <= %d")),
                            StringLimits.LAST_NAME.min(), StringLimits.LAST_NAME.max()));

            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        final char[] password = passwordTextField.getPassword();

        if (!StringPatterns.STRONG_PASSWORD.matcher(CharBuffer.wrap(password)).find()) {
            passwordTextField.putClientProperty("JComponent.outline", "error");
            passwordError.setText("<html>Password must have 8+ characters with upper, lower, number, and special"
                    + " character.</html>");

            return false;
        }

        return true;
    }

    @Override
    public void formAfterOpen() {
        firstNameTextField.requestFocusInWindow();
    }

    @Override
    public void formClose() {
        firstNameTextField.removeKeyListener(enterKeyListener);
        lastNameTextField.removeKeyListener(enterKeyListener);
        displayNameTextField.removeKeyListener(enterKeyListener);
        emailTextField.removeKeyListener(enterKeyListener);
        passwordTextField.removeKeyListener(enterKeyListener);
        previousStepButton.removeActionListener(previousStepButtonListener);
        loginButton.removeActionListener(loginButtonActionListener);
        registerButton.removeActionListener(registerButtonActionListener);
    }

    @Override
    public void formOpen() {
        firstNameTextField.addKeyListener(enterKeyListener);
        lastNameTextField.addKeyListener(enterKeyListener);
        displayNameTextField.addKeyListener(enterKeyListener);
        emailTextField.addKeyListener(enterKeyListener);
        passwordTextField.addKeyListener(enterKeyListener);
        previousStepButton.addActionListener(previousStepButtonListener);
        loginButton.addActionListener(loginButtonActionListener);
        registerButton.addActionListener(registerButtonActionListener);
    }

    private class EnterKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(final KeyEvent e) {
            if (e.getKeyCode() != KeyEvent.VK_ENTER) {
                return;
            }

            final Object source = e.getSource();

            if (source == firstNameTextField && validateFirstName()) {
                firstNameTextField.putClientProperty("JComponent.outline", null);
                firstNameError.setText("");

                if (lastNameTextField.getText().isEmpty()) {
                    lastNameTextField.requestFocusInWindow();
                } else {
                    registerButton.doClick();
                }
            } else if (source == lastNameTextField && validateLastName()) {
                lastNameTextField.putClientProperty("JComponent.outline", null);
                lastNameError.setText("");

                if (firstNameTextField.getText().isEmpty()) {
                    firstNameTextField.requestFocusInWindow();
                } else {
                    registerButton.doClick();
                }
            } else if (source == displayNameTextField && validateDisplayName()) {
                displayNameTextField.putClientProperty("JComponent.outline", null);
                displayNameError.setText("");

                if (emailTextField.getText().isEmpty()) {
                    emailTextField.requestFocusInWindow();
                } else {
                    registerButton.doClick();
                }
            } else if (source == emailTextField && validateEmail()) {
                emailTextField.putClientProperty("JComponent.outline", null);
                emailError.setText("");

                if (displayNameTextField.getText().isEmpty()) {
                    displayNameTextField.requestFocusInWindow();
                } else {
                    registerButton.doClick();
                }
            } else if (source == passwordTextField && validatePassword()) {
                passwordTextField.putClientProperty("JComponent.outline", null);
                passwordError.setText("");

                registerButton.doClick();
            }
        }
    }

    private class LoginButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            final Form form = new FormAuthLogin();

            form.formInit();
            FormManager.showAuthForm(form);
        }
    }

    private class PreviousStepButtonListener implements ActionListener {
        private final JPanel owner;

        public PreviousStepButtonListener(final JPanel owner) {
            this.owner = owner;
        }

        private void stepOne() {
            // no op, shouldnt be reached
        }

        private void stepThree() {
            isBusy.set(true);

            registerButton.setIcon(cachedRegisterButtonIcon);
            registerButton.setText("Continue");

            paneSlider.addSlide(step2Panel, SlidePaneTransition.create(SlidePaneTransition.Type.BACK),
                    new SliderCallback() {
                        @Override
                        public void complete() {
                            stepLabel.setText(String.format("Step: %s", currentStep.decrementAndGet()));
                            emailTextField.requestFocusInWindow();
                            isBusy.set(false);
                        }
                    });
        }

        private void stepTwo() {
            isBusy.set(true);

            contentContainer.remove(previousStepButton);
            contentContainer.remove(registerButton);
            contentContainer.add(registerButton, "gapy 20", 4);

            contentContainer.repaint();
            contentContainer.revalidate();

            paneSlider.addSlide(step1Panel, SlidePaneTransition.create(SlidePaneTransition.Type.BACK),
                    new SliderCallback() {
                        @Override
                        public void complete() {
                            stepLabel.setText(String.format("Step: %s", currentStep.decrementAndGet()));
                            lastNameTextField.requestFocusInWindow();
                            isBusy.set(false);
                        }
                    });
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (isBusy.get()) {
                return;
            }

            switch (currentStep.get()) {
                case 1:
                    stepOne();
                    break;
                case 2:
                    stepTwo();
                    break;
                case 3:
                    stepThree();
                    break;
                default:
                    JOptionPane.showMessageDialog(owner, "Something went wrong");
            }
        }
    }

    private class RegisterButtonActionListener implements ActionListener {
        private final JPanel owner;

        public RegisterButtonActionListener(final JPanel owner) {
            this.owner = owner;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (isBusy.get()) {
                return;
            }

            switch (currentStep.get()) {
                case 1:
                    stepOne();
                    break;
                case 2:
                    stepTwo();
                    break;
                case 3:
                    stepThree();
                    break;
                default:
                    JOptionPane.showMessageDialog(owner, "Something went wrong");
            }
        }

        private void stepOne() {
            clearStep1Errors();

            if (!validateFirstName() | !validateLastName()) {
                return;
            }

            isBusy.set(true);

            contentContainer.remove(registerButton);
            contentContainer.add(previousStepButton, "gapy 20", 4);
            contentContainer.add(registerButton, "gapy 5", 5);

            contentContainer.repaint();
            contentContainer.revalidate();

            paneSlider.addSlide(step2Panel, SlidePaneTransition.create(SlidePaneTransition.Type.FORWARD),
                    new SliderCallback() {
                        @Override
                        public void complete() {
                            stepLabel.setText(String.format("Step: %s", currentStep.incrementAndGet()));
                            displayNameTextField.requestFocusInWindow();
                            isBusy.set(false);
                        }
                    });
        }

        private void stepThree() {
            clearStep3Errors();

            if (!validatePassword()) {
                return;
            }

            isBusy.set(true);

            final AuthenticationStatus status = Authentication.signUp(displayNameTextField.getText(),
                    firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(),
                    passwordTextField.getPassword());

            switch (status.getStatusType()) {
                case SUCCESS -> {
                    JOptionPane.showMessageDialog(owner, "Your account has been registered. Please sign in.",
                            "Sign Up Success :)", JOptionPane.INFORMATION_MESSAGE);

                    FormManager.showAuthForm(new FormAuthLogin());
                }
                case ERROR -> {
                    JOptionPane.showMessageDialog(owner, status.getMessage(), "Sign Up Failure :(",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            isBusy.set(false);
        }

        private void stepTwo() {
            clearStep2Errors();

            if (!validateDisplayName() | !validateEmail()) {
                return;
            }

            isBusy.set(true);

            cachedRegisterButtonIcon = registerButton.getIcon();
            registerButton.setIcon(null);
            registerButton.setText("Sign Up");

            paneSlider.addSlide(step3Panel, SlidePaneTransition.create(SlidePaneTransition.Type.FORWARD),
                    new SliderCallback() {
                        @Override
                        public void complete() {
                            stepLabel.setText(String.format("Step: %s", currentStep.incrementAndGet()));
                            passwordTextField.requestFocusInWindow();
                            isBusy.set(false);
                        }
                    });
        }
    }
}
