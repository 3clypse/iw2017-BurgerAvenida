package com.pd.vaadin;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.service.security.SecurityService;
import com.pd.vaadin.view.AboutView;
import com.pd.vaadin.view.CreateUserView;
import com.pd.vaadin.view.HomeView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
@SpringViewDisplay
public class MainScreen extends HorizontalLayout implements ViewDisplay {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9218789120244632393L;
	
	private static final String VALO_MENUITEMS = "valo-menuitems";
    private static final String VALO_MENU_TOGGLE = "valo-menu-toggle";
    private static final String VALO_MENU_VISIBLE = "valo-menu-visible";
    
    private CssLayout menuItemsLayout;
    private CssLayout menuPart;
    
	private Panel springViewDisplay;
	
	@Autowired
	SecurityService securityService;
	
	@Override
    public void attach() {
        super.attach();
        this.getUI().getNavigator().navigateTo(HomeView.VIEW_NAME);
    }
	
	@PostConstruct
	void init() {
		setSpacing(false);
        setStyleName("main-screen");
        CssLayout menu = new CssLayout();
        
        menu.setPrimaryStyleName(ValoTheme.MENU_ROOT);
        menuPart = new CssLayout();
        menuPart.addStyleName(ValoTheme.MENU_PART);

        final HorizontalLayout top = new HorizontalLayout();
        top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        top.addStyleName(ValoTheme.MENU_TITLE);
        Label title = new Label("MENU");
        title.addStyleName(ValoTheme.LABEL_H3);
        title.setSizeUndefined();
        Image image = new Image(null, new ThemeResource("img/table-logo.png"));
        image.setStyleName("logo");
        top.addComponent(image);
        top.addComponent(title);
        menuPart.addComponent(top);

        MenuBar logoutMenu = new MenuBar();
        logoutMenu.addItem("Logout", VaadinIcons.SIGN_OUT, selectedItem -> {
        	logout();
		});

        logoutMenu.addStyleName("user-menu");
        menuPart.addComponent(logoutMenu);

        final Button showMenu = new Button("Menu", (ClickListener) event -> {
		    if (menuPart.getStyleName().contains(VALO_MENU_VISIBLE)) {
		        menuPart.removeStyleName(VALO_MENU_VISIBLE);
		    } else {
		        menuPart.addStyleName(VALO_MENU_VISIBLE);
		    }
		});
        showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
        showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
        showMenu.addStyleName(VALO_MENU_TOGGLE);
        showMenu.setIcon(VaadinIcons.MENU);
        menuPart.addComponent(showMenu);
        
        menuItemsLayout = new CssLayout();
                menuItemsLayout.addComponent(
        		createNavigationButton(
        				"Home", 
        				HomeView.VIEW_NAME,
        				VaadinIcons.HOME));
                //if(securityService.hasRole(RoleName.ROLE_ATTENDANT))
        		menuItemsLayout.addComponent(
                		createNavigationButton(
                		"About", 
                		AboutView.VIEW_NAME,
                		VaadinIcons.INFO));
        		//if(securityService.hasRole(RoleName.ROLE_MANAGER));
        		
        		Button button = new Button("ADMIN ZONE");
        		button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
                //button.setIcon(VaadinIcons.MENU);
                button.setEnabled(false);
                menuItemsLayout.addComponent(button);
                
        		
        		menuItemsLayout.addComponent(
                		createNavigationButton(
                		"Create User", 
                		CreateUserView.VIEW_NAME,
                		VaadinIcons.USER));
        menuItemsLayout.setPrimaryStyleName(VALO_MENUITEMS);
        
        menuPart.addComponent(menuItemsLayout);
        menu.addComponent(menuPart);
        addComponent(menu);
        
        springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();
        addComponent(springViewDisplay);
        setExpandRatio(springViewDisplay, 1.0f);
        setSizeFull();
	}

	private Button createNavigationButton(String caption, final String viewName, VaadinIcons icon) {
		Button button = new Button(caption);
		button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
        button.setIcon(icon);
		button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
		return button;
	}
	
	@Override
	public void showView(View view) {
		springViewDisplay.setContent((Component) view);
	}
	
	private void logout() {
		securityService.logout();
		getUI().getPage().reload();
		getSession().close();
	}

}
