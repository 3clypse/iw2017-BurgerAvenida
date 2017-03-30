package com.pd.vaadin;


import com.pd.vaadin.about.AboutView;
import com.pd.vaadin.contact.ContactView;
import com.pd.vaadin.home.HomeView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
public class MainScreen extends HorizontalLayout {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -116841406790508949L;
	
	private Menu menu;

    public MainScreen(MyUI ui) {

        setSpacing(false);
        setStyleName("main-screen");

        CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();

        final Navigator navigator = new Navigator(ui, viewContainer);
        navigator.setErrorView(ErrorView.class);
        menu = new Menu(navigator);
        menu.addView(
        		new HomeView(),
        		HomeView.VIEW_NAME,
        		"Home",
        		VaadinIcons.HOME);
        menu.addView(
        		new ContactView(), 
        		ContactView.VIEW_NAME,
                ContactView.VIEW_NAME, 
                VaadinIcons.EDIT);
        menu.addView(
        		new AboutView(), 
        		AboutView.VIEW_NAME, 
        		AboutView.VIEW_NAME,
                VaadinIcons.INFO_CIRCLE);

        navigator.addViewChangeListener(viewChangeListener);

        addComponent(menu);
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1);
        setSizeFull();
        System.out.println(navigator.getState());
    }

    // notify the view menu about view changes so that it can display which view
    // is currently active
    ViewChangeListener viewChangeListener = new ViewChangeListener() {

        /**
		 * 
		 */
		private static final long serialVersionUID = -6481036683178672759L;

		@Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }

    };
}
