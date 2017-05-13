package com.pd.vaadin.view.client;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.ClientDao;
import com.pd.model.Client;
import com.pd.model.ProductFamily;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringComponent
public class ClientEditor extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1700349086596241763L;

	
	private final ClientDao repository;

	/**
	 * The currently object
	 */
	private Client currentObject;

	TextField name = new TextField("Name");
	TextField phoneNumber = new TextField("Phone number");
	TextField address = new TextField("address");

	Button save = new Button("Save", VaadinIcons.SAFE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcons.ERASER);
	CssLayout actions = new CssLayout(save, cancel, delete);

	Binder<Client> binder = new Binder<>(Client.class);

	@Autowired
	public ClientEditor(ClientDao repository) {
		this.repository = repository;
		
		name.setSizeFull();
		phoneNumber.setSizeFull();
		address.setSizeFull();
		
		addComponents(name, phoneNumber, address, actions);

		binder.bindInstanceFields(this);

		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		save.addClickListener(e -> repository.save(currentObject));
		delete.addClickListener(e -> repository.delete(currentObject));
		cancel.addClickListener(e -> setVisible(false));
		setVisible(false);
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void edit(Client c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			currentObject = repository.findOne(c.getId());
		}
		else {
			currentObject = c;
		}
		cancel.setVisible(persisted);

		binder.setBean(currentObject);

		setVisible(true);

		save.focus();
		name.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}
	
}