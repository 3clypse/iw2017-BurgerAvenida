package com.pd.vaadin.view.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.security.RoleDao;
import com.pd.dao.security.UserDao;
import com.pd.model.security.Role;
import com.pd.model.security.User;
import com.pd.service.security.UserService;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
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
	

	/**
	 * The currently object
	 */
	private User currentObject;

	TextField username = new TextField("Username");
	PasswordField password = new PasswordField("Password");
	TextField firstname = new TextField("Firstname");
	TextField lastname = new TextField("Lastname");
	TextField email = new TextField("Email");
	TwinColSelect<Role> roles = new TwinColSelect<>("Roles");

	Button save = new Button("Save", VaadinIcons.SAFE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcons.ERASER);
	CssLayout actions = new CssLayout(save, cancel, delete);

	Binder<User> binder;

	@Autowired
	public UserEditor(UserDao repository, RoleDao roleDao, UserService userService) {
		this.repository = repository;
		this.roleDao = roleDao;
		this.userService = userService;

		roles.setItems((Collection<Role>) roleDao.findAll());
		addComponents(username, password, firstname, lastname, email, roles, actions);
		
		binder = new BeanValidationBinder<>(User.class);
		binder.bindInstanceFields(this);

		username.setSizeFull();
		password.setSizeFull();
		firstname.setSizeFull();
		lastname.setSizeFull();
		email.setSizeFull();
		roles.setSizeFull();
		
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		save.addClickListener(e -> userService.save(currentObject));
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
		
		binder.setBean(currentObject);
		
		setVisible(true);
	}
	
	public void setChangeHandler(ChangeHandler h) {
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}
	
}