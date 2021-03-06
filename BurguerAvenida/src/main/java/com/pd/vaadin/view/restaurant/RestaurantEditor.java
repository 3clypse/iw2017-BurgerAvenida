package com.pd.vaadin.view.restaurant;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.RestaurantDao;
import com.pd.dao.security.UserDao;
import com.pd.model.Restaurant;
import com.pd.model.Zone;
import com.pd.model.security.RoleName;
import com.pd.model.security.User;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringComponent
public class RestaurantEditor extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1700349086596241763L;

	
	private final RestaurantDao repository;
	
	@SuppressWarnings("unused")
	private final UserDao userDao;
	

	/**
	 * The currently object
	 */
	private Restaurant currentObject;

	TextField name = new TextField("Name");
	TextField address = new TextField("Address");
	ComboBox<User> attendants = new ComboBox<>("Attendant");

	Button save = new Button("Save", VaadinIcons.SAFE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcons.ERASER);
	CssLayout actions = new CssLayout(save, cancel, delete);

	Binder<Restaurant> binder;

	@Autowired
	public RestaurantEditor(RestaurantDao repository, UserDao userDao) {
		this.repository = repository;
		this.userDao = userDao;

		attendants.setItems(userDao.findByRole(RoleName.ROLE_ATTENDANT));
		attendants.setEmptySelectionAllowed(false);
		addComponents(name, address, attendants, actions);
		
		binder = new BeanValidationBinder<>(Restaurant.class);
		binder.forField(attendants).bind("attendant");
		//binder.bindInstanceFields(this);

		name.setSizeFull();
		address.setSizeFull();
		attendants.setSizeFull();
		
		name.setMaxLength(32);
		address.setMaxLength(64);
		
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		
		binder.forField(name)
		.asRequired("Cant be empty")
	    .withValidator(new StringLengthValidator(
	        "Address must be between 2 and 32 characters long",
	        2, 32))
	    .bind(Restaurant::getName, Restaurant::setName);
		
		binder.forField(address)
		.asRequired("Cant be empty")
	    .withValidator(new StringLengthValidator(
	        "Address must be between 2 and 64 characters long",
	        2, 64))
	    .bind(Restaurant::getAddress, Restaurant::setAddress);

		//save.addClickListener(e -> repository.save(currentObject));
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

	public final void edit(Restaurant c) {
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
	}
	
	public void setChangeHandler(ChangeHandler h) {
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}
	
	private void showNotification(Notification notification) {
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }
	
}