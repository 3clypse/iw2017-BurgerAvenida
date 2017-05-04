package com.pd.vaadin.view.user;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.pd.dao.security.UserDao;
import com.pd.model.security.User;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = UserView.VIEW_ROUTE)
public class UserView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6053541745106875920L;

	public static final String VIEW_ROUTE = "UserView";
	public static final String VIEW_NAME = "UserView";

	private final UserDao repo;

	private final UserEditor editor;

	final Grid<User> grid;

	final TextField filter;

	private final Button addNewBtn;

	@Autowired
	public UserView(UserDao repo, UserEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(User.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New User", VaadinIcons.PLUS_CIRCLE_O);
	}
	
	@PostConstruct
	void init() {
		this.setResponsive(true);
		Label header = new Label("Users");
		header.addStyleName(ValoTheme.LABEL_H2);
		header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		VerticalLayout verticalTitleLayout = new VerticalLayout(new HorizontalLayout(header));
		verticalTitleLayout.setMargin(new MarginInfo(false, false, false, true));
		addComponent(verticalTitleLayout);
		
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		addComponent(mainLayout);

		grid.setSizeFull();
		grid.setColumns("username", "firstname", "lastname", "email", "roles");

		filter.setPlaceholder("Filter by Username");

		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> list(e.getValue()));
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.edit(e.getValue());
		});
		addNewBtn.addClickListener(e -> editor.edit(new User()));
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			list(filter.getValue());
		});
		list(null);
        
    }
    
    void list(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems((Collection<User>) repo.findAll());
		}
		else {
			grid.setItems(repo.findByUsernameStartsWithIgnoreCase(filterText));
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
