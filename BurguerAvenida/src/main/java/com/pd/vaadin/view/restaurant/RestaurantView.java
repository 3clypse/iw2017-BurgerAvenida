package com.pd.vaadin.view.restaurant;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.pd.dao.RestaurantDao;
import com.pd.model.Restaurant;
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
@SpringView(name = RestaurantView.VIEW_ROUTE)
public class RestaurantView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6053541745106875920L;

	public static final String VIEW_ROUTE = "RestaurantView";
	public static final String VIEW_NAME = "RestaurantView";

	private final RestaurantDao repo;

	private final RestaurantEditor editor;

	final Grid<Restaurant> grid;

	final TextField filter;

	private final Button addNewBtn;

	@Autowired
	public RestaurantView(RestaurantDao repo, RestaurantEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(Restaurant.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New Restaurant", VaadinIcons.PLUS_CIRCLE_O);
	}
	
	@PostConstruct
	void init() {
		this.setResponsive(true);
		Label header = new Label("Restaurants");
		header.addStyleName(ValoTheme.LABEL_H2);
		header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		VerticalLayout verticalTitleLayout = new VerticalLayout(new HorizontalLayout(header));
		verticalTitleLayout.setMargin(new MarginInfo(false, false, false, true));
		addComponent(verticalTitleLayout);
		
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		addComponent(mainLayout);

		grid.setSizeFull();
		grid.setColumns("name", "address", "attendant");

		filter.setPlaceholder("Filter by name");

		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> list(e.getValue()));
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.edit(e.getValue());
		});
		addNewBtn.addClickListener(e -> editor.edit(new Restaurant()));
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			list(filter.getValue());
		});
		list(null);
        
    }
    
    void list(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems((Collection<Restaurant>) repo.findAll());
		}
		else {
			grid.setItems(repo.findByNameStartsWithIgnoreCase(filterText));
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
