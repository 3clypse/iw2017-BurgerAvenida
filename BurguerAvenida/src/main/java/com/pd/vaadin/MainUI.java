package com.pd.vaadin;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.service.security.SecurityService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("mytheme")
@SpringUI
public class MainUI extends UI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1465336204444520218L;

	@Autowired
	SecurityService securityService;

	@Autowired
	SpringViewProvider viewProvider;
	
	@Autowired
    MainScreen mainScreen;
	
	@Override
	protected void init(VaadinRequest request) {
		Responsive.makeResponsive(this);
        getPage().setTitle("Burguer Avenida App");
        addStyleName(ValoTheme.UI_WITH_MENU);
		this.getUI().getNavigator().setErrorView(ErrorView.class);
		//viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
	
		if (securityService.findLoggedInUsername() != null) {
			showMainScreen();
		} else {
			showLoginScreen();
		}
	}
	
	private void showLoginScreen() {
		setContent(new LoginScreen(this::login));
	}

	private void showMainScreen() {
		setContent(mainScreen);
	}
	
	protected boolean login(String username, String password) {
		if(securityService.autologin(username, password)){
			showMainScreen();
			return true;
		}else return false;
	}
}  
