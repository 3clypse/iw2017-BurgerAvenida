package com.pd.vaadin.view.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.RestaurantDao;
import com.pd.dao.security.RoleDao;
import com.pd.dao.security.UserDao;
import com.pd.model.Restaurant;
import com.pd.model.security.Role;
import com.pd.model.security.User;
import com.pd.service.security.UserService;
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
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringComponent
public class UserEditor extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1700349086596241763L;

	
	private final UserDao repository;
	
	@SuppressWarnings("unused")
	private final RoleDao roleDao;
	
	@SuppressWarnings("unused")
	private final UserService userService;
	
	@SuppressWarnings("unused")
	private final RestaurantDao restaurantDao;

	/**
	 * The currently object
	 */
	private User currentObject;

	TextField username = new TextField("Username");
	PasswordField password = new PasswordField("Password");
	TextField firstname = new TextField("Firstname");
	TextField lastname = new TextField("Lastname");
	TextField email = new TextField("Email");
	ComboBox<Restaurant> workin = new ComboBox<>("Work in");
	TwinColSelect<Role> roles = new TwinColSelect<>("Roles");

	Button save = new Button("Save", VaadinIcons.SAFE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcons.ERASER);
	CssLayout actions = new CssLayout(save, cancel, delete);

	Binder<User> binder;

	@Autowired
	public UserEditor(UserDao repository, RoleDao roleDao, UserService userService, RestaurantDao restaurantDao) {
		this.repository = repository;
		this.roleDao = roleDao;
		this.userService = userService;
		this.restaurantDao = restaurantDao;

		roles.setItems((Collection<Role>) roleDao.findAll());
		workin.setItems((Collection<Restaurant>) restaurantDao.findAll());
		
		
		addComponents(username, password, firstname, lastname, email, workin, roles, actions);
		binder = new BeanValidationBinder<>(User.class);
		binder.forField(workin).bind("workin");
		binder.bindInstanceFields(this);

		username.setSizeFull();
		password.setSizeFull();
		firstname.setSizeFull();
		lastname.setSizeFull();
		email.setSizeFull();
		workin.setSizeFull();
		roles.setSizeFull();
		
		username.setMaxLength(16);
		password.setMaxLength(32);
		firstname.setMaxLength(16);
		lastname.setMaxLength(32);
		email.setMaxLength(32);
		
		binder.forField(username)
		.asRequired("Cant be empty")
	    .withValidator(new StringLengthValidator(
	        "Name must be between 2 and 16 characters long",
	        2, 16))
	    .bind(User::getUsername, User::setUsername);
		
		binder.forField(password)
		.asRequired("Cant be empty")
	    .withValidator(new StringLengthValidator(
	        "Passowrd must be between 8 and 32 characters long",
	        8, 128))
	    .bind(User::getPassword, User::setPassword);
		
		binder.forField(firstname)
		.asRequired("Cant be empty")
	    .withValidator(new StringLengthValidator(
	        "Firstname must be between 2 and 16 characters long",
	        2, 16))
	    .bind(User::getFirstname, User::setFirstname);
		
		binder.forField(lastname)
		.asRequired("Cant be empty")
	    .withValidator(new StringLengthValidator(
	        "Lastname must be between 2 and 32 characters long",
	        2, 32))
	    .bind(User::getLastname, User::setLastname);
		
		binder.forField(email)
		.asRequired("Cant be empty")
	    .withValidator(new StringLengthValidator(
	        "Email must be between 2 and 32 characters long",
	        2, 32))
	    .bind(User::getEmail, User::setEmail);

		
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		save.addClickListener(e -> {
			if(binder.isValid())
				userService.save(currentObject);
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

	public final void edit(User c) {
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