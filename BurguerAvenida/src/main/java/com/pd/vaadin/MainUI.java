package com.pd.vaadin;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.service.security.SecurityService;
import com.pd.vaadin.utils.ErrorView;
import com.pd.vaadin.utils.UnauthorizedView;
import com.pd.vaadin.view.HomeView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("mytheme")
@SpringUI
@PreserveOnRefresh
@Push(value = PushMode.AUTOMATIC, transport = Transport.WEBSOCKET_XHR)
public class MainUI extends UI implements Broadcaster.BroadcastListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1465336204444520218L;

	@WebServlet(value = { "/*", "/VAADIN/*" }, asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MainUI.class)
	public static class MyServlet extends SpringVaadinServlet {
		private static final long serialVersionUID = -7398761406150484727L;
	}
	
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

		getUI().getNavigator().setErrorView(ErrorView.class);
		viewProvider.setAccessDeniedViewClass(UnauthorizedView.class);
		if (securityService.isLoggedIn()) {
			showMainScreen();
		} else {
			Page.getCurrent().setUriFragment("!");
			showLoginScreen();
		}
		Broadcaster.register(this);
	}

	private void showLoginScreen() {
		setContent(new LoginScreen(this::login));
	}

	private void showMainScreen() {
		setContent(mainScreen);
	}

	protected boolean login(String username, String password) {
		if (securityService.autologin(username, password)) {
			showMainScreen();
			return true;
		} else
			return false;
	}

	@Override
	public void detach() {
		Broadcaster.unregister(this);
		super.detach();
	}

	@Override
	public void receiveBroadcast(String msg) {
		getSession().lock();
		getUI().access(() -> {
			if(msg.startsWith("LOCAL")) {
				String orderLineStr = msg.replace("LOCAL", "");
				HomeView.orderLineSetLocal.add(orderLineStr);
				HomeView.orderListLocal.setItems(HomeView.orderLineSetLocal);
			}
			if(msg.startsWith("HOMEDELIVERY")) {
				String orderLineStr = msg.replace("HOMEDELIVERY", "");
				HomeView.orderLineSetHome.add(orderLineStr);
				HomeView.orderListHome.setItems(HomeView.orderLineSetHome);
			}
			if(msg.startsWith("TOTAKEAWAY")) {
				String orderLineStr = msg.replace("TOTAKEAWAY", "");
				HomeView.orderLineSetAway.add(orderLineStr);
				HomeView.orderListAway.setItems(HomeView.orderLineSetAway);
			}
		});
		getSession().unlock();
	}

}
