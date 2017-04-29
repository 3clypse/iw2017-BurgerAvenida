package com.pd.vaadin.about;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dto.UserListDto;
import com.pd.service.security.UserService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = AboutView.VIEW_ROUTE)
public class AboutView extends VerticalLayout implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = -678558853372417010L;

	@Autowired
	private UserService userService;
	
	private Grid<UserListDto> grid;
	
	
	public static final String VIEW_ROUTE = "About";
	public static final String VIEW_NAME = "About";
	
    @PostConstruct
    void init() {
    	
    	setSizeFull();
        Label header = new Label("About");
        header.addStyleName(ValoTheme.LABEL_H2);
        addComponent(header);
    	
    	HorizontalLayout options = new HorizontalLayout();
    	options.setWidth("100%");
    	addComponent(options);
    	
    	grid = new Grid<UserListDto>();
    	grid.setSizeFull();
    	addComponent(grid);
    	setExpandRatio(grid, 1);

    	List<UserListDto> users = userService.readAll();
    	grid.setItems(users);
    	
    	grid.addColumn(UserListDto::getUsername).setCaption("Username");
    	grid.addColumn(UserListDto::getFirstname).setCaption("Firstname");
    	grid.addColumn(UserListDto::getLastname).setCaption("Lastname");
    	grid.addColumn(UserListDto::getEmail).setCaption("Email");
    }

    @Override
    public void enter(ViewChangeEvent event) {
    	
    }

}
