package com.pd.view;

import java.util.Arrays;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.ClientDao;
import com.pd.model.Client;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@SpringUI(path = "")
public class Index extends UI {

	/**
	* 
	*/
	private static final long serialVersionUID = -6457669656179486858L;

	private final ClientDao repo;

	final Grid<Client> grid;

	final TextField filter;

	private final Button addNewBtn;
	
	@Autowired
	public Index(ClientDao repo) {
		this.repo = repo;
		this.grid = new Grid<>(Client.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New customer");
		repo.save(Arrays.asList(
			new Client(1, "Nombre 1", 633455623, "Calle 1"),
			new Client(2, "Nombre 2", 634321232, "Calle 2"),
			new Client(3, "Nombre 3", 324324323, "Calle 3")
		));
	}
	
	@Override
	protected void init(VaadinRequest request) {
		
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid);
		setContent(mainLayout);
		
		grid.setCaption("Client info");
		grid.setWidth(100, Unit.PERCENTAGE);
		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "name", "phoneNumber", "address");
		grid.setItems(Lists.newArrayList(repo.findAll()));

	}

}
