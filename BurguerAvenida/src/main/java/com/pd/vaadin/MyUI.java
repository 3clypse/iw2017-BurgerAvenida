package com.pd.vaadin;

import javax.servlet.annotation.WebServlet;

import com.pd.vaadin.about.AboutView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("mytheme")
@SpringUI
public class MyUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -894042507103698002L;
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Responsive.makeResponsive(this);
        setLocale(vaadinRequest.getLocale());
        getPage().setTitle("Burguer Avenida App");
        showMainView();
    }
    
    protected  void showMainView() {
    	addStyleName(ValoTheme.UI_WITH_MENU);
        setContent(new MainScreen(MyUI.this));
        getNavigator().navigateTo(AboutView.VIEW_NAME);
    }

    public static MyUI get() {
        return (MyUI) UI.getCurrent();
    }
    
    @WebServlet(urlPatterns = "/**", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1481329815797547221L;
    }
	
}
