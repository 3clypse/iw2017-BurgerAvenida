package com.pd.vaadin;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * UI content when the user is not logged in yet.
 */
@UIScope
public class LoginScreen extends CssLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8238093631698853620L;

	public static final String VIEW_NAME = "Login";
	public static String VIEW_ROUTE = "Login";

	private TextField username;
	private PasswordField password;
	private Button login;
	private Button forgotPassword;

	@FunctionalInterface
	public interface LoginCallback {

		boolean login(String username, String password);
	}

	public LoginScreen(LoginCallback callback) {
		addStyleName("login-screen");

		Component loginForm = buildLoginForm(callback);

		VerticalLayout centeringLayout = new VerticalLayout();
		centeringLayout.setMargin(false);
		centeringLayout.setSpacing(false);
		centeringLayout.setStyleName("centering-layout");
		centeringLayout.addComponent(loginForm);
		centeringLayout.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

		CssLayout loginInformation = buildLoginInformation();

		addComponent(centeringLayout);
		addComponent(loginInformation);
	}

	private Component buildLoginForm(LoginCallback callback) {
		FormLayout loginForm = new FormLayout();

		loginForm.addStyleName("login-form");
		loginForm.setSizeUndefined();
		loginForm.setMargin(false);

		loginForm.addComponent(username = new TextField("username", "Introduzca su usuario"));
		username.setWidth(15, Unit.EM);
		loginForm.addComponent(password = new PasswordField("password"));
		password.setWidth(15, Unit.EM);
		password.setDescription("Write anything");
		CssLayout buttons = new CssLayout();
		buttons.setStyleName("buttons");
		loginForm.addComponent(buttons);

		buttons.addComponent(login = new Button("Login"));
		login.setDisableOnClick(true);
		login.addClickListener(event -> {
			try {
				login.setEnabled(false);
				String pword = password.getValue();
				password.setValue("");
				if (!callback.login(username.getValue(), pword)) {
					showNotification(new Notification("Usuario o contraseña incorrecto"));
					username.focus();
				}
			} finally {
				login.setEnabled(true);
			}
		});

		login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		login.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		buttons.addComponent(forgotPassword = new Button("Forgot password?"));
		forgotPassword.addClickListener(event -> showNotification(new
		Notification("Hint: Try anything")));
		forgotPassword.addStyleName(ValoTheme.BUTTON_LINK);
		return loginForm;
	}
	
	private void showNotification(Notification notification) {
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }
	
	private CssLayout buildLoginInformation() {
		CssLayout loginInformation = new CssLayout();
		loginInformation.setStyleName("login-information");
		Label loginInfoText = new Label(
				"<h1>Login Information</h1>"
						+ "Log in as &quot;admin&quot; to have full access. Log in with any other username to have read-only access. For all users, any password is fine",
				ContentMode.HTML);
		loginInfoText.setSizeFull();
		loginInformation.addComponent(loginInfoText);
		return loginInformation;
	}
}
