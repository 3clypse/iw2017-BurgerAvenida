package com.pd.vaadin.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.security.UserDao;
import com.pd.service.security.SecurityService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = HomeView.VIEW_ROUTE)
public class HomeView extends VerticalLayout implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6053541745106875920L;
	
	public static final String VIEW_ROUTE = "Home";
	public static final String VIEW_NAME = "Home";
	
	@Autowired
	SecurityService securityService;
	
	@Autowired
	UserDao userDao;
	
	@PostConstruct
    void init() {
    	 setSizeFull();
         Label header = new Label("Home");
         header.addStyleName(ValoTheme.LABEL_H2);
         addComponent(header);
         
         ListSelect<String> select = new ListSelect<>("The List");

      // Add some items
	      select.setItems("Mercury", "Venus", "Earth");
	
	      // Show 5 items and a scrollbar if there are more
	      select.setRows(5);
	
	      select.select("Mercury");
	      addComponent(select);
         
         //System.out.println("Attendant:" +securityService.hasRole(RoleName.ROLE_ATTENDANT));
         //System.out.println("Waiter:" +securityService.hasRole(RoleName.ROLE_WAITER));
         //System.out.println("Manager:" +securityService.hasRole(RoleName.ROLE_MANAGER));
         
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }

}