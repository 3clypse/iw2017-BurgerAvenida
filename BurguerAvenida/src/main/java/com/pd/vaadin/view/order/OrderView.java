package com.pd.vaadin.view.order;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.pd.dao.ClientDao;
import com.pd.dao.OrderDao;
import com.pd.dao.ZoneDao;
import com.pd.model.Client;
import com.pd.model.Order;
import com.pd.model.Zone;
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
@SpringView(name = OrderView.VIEW_ROUTE)
public class OrderView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6053541745106875920L;

	public static final String VIEW_ROUTE = "OrderView";
	public static final String VIEW_NAME = "OrderView";

	private final OrderDao repo;

	private final OrderEditor editor;
	
	private final ZoneDao zoneDao;
	
	private final ClientDao clientDao;

	final Grid<Order> grid;

	final TextField filter;

	private final Button addNewBtn;

	@Autowired
	public OrderView(OrderDao repo, OrderEditor editor, ZoneDao zoneDao, ClientDao clientDao) {
		this.repo = repo;
		this.editor = editor;
		this.zoneDao = zoneDao;
		this.clientDao = clientDao;
		this.grid = new Grid<>(Order.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New order", VaadinIcons.PLUS_CIRCLE_O);
	}
	
	@PostConstruct
	void init() {
		this.setResponsive(true);
		Label header = new Label("Orders");
		header.addStyleName(ValoTheme.LABEL_H2);
		header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		VerticalLayout verticalTitleLayout = new VerticalLayout(new HorizontalLayout(header));
		verticalTitleLayout.setMargin(new MarginInfo(false, false, false, true));
		addComponent(verticalTitleLayout);
		
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		addComponent(mainLayout);

		grid.setSizeFull();
		grid.setColumns("createdAt", "closedAt", "status", "type", "zone", "client");

		filter.setPlaceholder("Filter by name");

		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> list(e.getValue()));
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.edit(e.getValue());
		});
		addNewBtn.addClickListener(e -> editor.edit(new Order()));
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			list(filter.getValue());
		});
		list(null);
        
    }
    
    void list(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems((Collection<Order>) repo.findAll());
		}
		else {
			//grid.setItems(repo.findByNameStartsWithIgnoreCase(filterText));
			grid.setItems((Collection<Order>) repo.findAll());
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		grid.setItems((Collection<Order>) repo.findAll());
		editor.zone.setItems((Collection<Zone>) zoneDao.findAll());
		editor.client.setItems((Collection<Client>) clientDao.findAll());
	}
}
