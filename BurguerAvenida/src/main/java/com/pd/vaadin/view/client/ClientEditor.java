package com.pd.vaadin.view.client;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.ClientDao;
import com.pd.model.Client;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
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
	TextField address = new TextField("Address");

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
		name.setMaxLength(32);
		phoneNumber.setMaxLength(9);
		address.setMaxLength(64);
		
		addComponents(name, phoneNumber, address, actions);

		binder.forField(address)
		.asRequired("Cant be empty")
	    .withValidator(new StringLengthValidator(
	        "Address must be between 4 and 64 characters long",
	        4, 64))
	    .bind(Client::getAddress, Client::setAddress);
		
		binder.forField(name)
		.asRequired("Cant be empty")
	    .withValidator(new StringLengthValidator(
	        "Name must be between 2 and 32 characters long",
	        2, 32))
	    .bind(Client::getName, Client::setName);
		
		binder.forField(phoneNumber)
		.asRequired("Cant be empty")
		.withValidator(new StringLengthValidator(
				"Phonenumber must have 9 digits", 9, 9))
		.withNullRepresentation("")
		  .withConverter(
		    new StringToIntegerConverter("Must enter a number"))
		  .bind(Client::getPhoneNumber, Client::setPhoneNumber);

		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		save.addClickListener(e -> {
			if(binder.isValid())
				repository.save(currentObject);
			else
				showNotification(new Notification("Some fields are not valid"));
		});
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

		if(currentObject != null)
			binder.setBean(currentObject);
		else{
			binder.writeBeanIfValid(currentObject);
		}

		setVisible(true);

		save.focus();
		name.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		save.addClickListener(e -> {
			if(binder.isValid())
				h.onChange();
		});
		delete.addClickListener(e -> h.onChange());
	}
	
	private void showNotification(Notification notification) {
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }
	
}