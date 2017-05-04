package com.pd.vaadin.view;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dto.UserPostDto;
import com.pd.exception.UserAlreadyExistsException;
import com.pd.model.security.RoleName;
import com.pd.model.security.User;
import com.pd.service.security.RoleService;
import com.pd.service.security.UserService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = CreateUserView.VIEW_ROUTE)
public class CreateUserView extends VerticalLayout implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6053541745106875920L;
	
	public static final String VIEW_ROUTE = "CreateUser";
	public static final String VIEW_NAME = "CreateUser";
	public static final String INVALID_FORM = "Invalid form";
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;

	private Set<RoleName> rolesSelected = new HashSet<RoleName>();
	private TextField usernameTf;
	private PasswordField passwordTf;
	private PasswordField passwordRepeatTf;
	private TextField firstnameTf;
	private TextField lastnameTf;
	private TextField emailTf;
	private User userCreated;
	private UserPostDto userPostDto;
	
	@PostConstruct
    void init() {
    	 
    	 HorizontalLayout title = new HorizontalLayout();
    	 
         Label header = new Label("User Creation Form");
         header.addStyleName(ValoTheme.LABEL_H2);
         title.addComponent(header);
         addComponent(title);
         
         HorizontalLayout content = new HorizontalLayout();
         FormLayout form = new FormLayout();
         
         usernameTf = new TextField("Username");
         usernameTf.setIcon(VaadinIcons.USER);
         usernameTf.setRequiredIndicatorVisible(true);
         form.addComponent(usernameTf);

         passwordTf = new PasswordField("Password");
         passwordTf.setIcon(VaadinIcons.PASSWORD);
         passwordTf.setRequiredIndicatorVisible(true);
         form.addComponent(passwordTf);
         
         passwordRepeatTf = new PasswordField("Repeat Password");
         passwordRepeatTf.setIcon(VaadinIcons.PASSWORD);
         passwordRepeatTf.setRequiredIndicatorVisible(true);
         form.addComponent(passwordRepeatTf);

         firstnameTf = new TextField("Firstname");
         firstnameTf.setIcon(VaadinIcons.INFO);
         firstnameTf.setRequiredIndicatorVisible(true);
         form.addComponent(firstnameTf);
         
         lastnameTf = new TextField("Lastname");
         lastnameTf.setIcon(VaadinIcons.INFO);
         lastnameTf.setRequiredIndicatorVisible(true);
         form.addComponent(lastnameTf);
         
         emailTf = new TextField("Email");
         emailTf.setIcon(VaadinIcons.MAILBOX);
         emailTf.setRequiredIndicatorVisible(true);
         form.addComponent(emailTf);
         
         ListSelect<RoleName> roleListSelect = new ListSelect<RoleName>("User roles");
         roleListSelect.setRows(3);
         roleListSelect.setItems(roleService.getRoleNames());
         roleListSelect.setIcon(VaadinIcons.BAN);
         roleListSelect.setRequiredIndicatorVisible(true);
         roleListSelect.addValueChangeListener(event -> {
        	 rolesSelected = event.getValue();
         });
         form.addComponent(roleListSelect);
         
         final Button createBtn = new Button("Submit", (ClickListener) event -> {
        	 try {
        		this.setEnabled(false);
        		if(isValidForm()) {
        			userPostDto = new UserPostDto(usernameTf.getValue(), passwordTf.getValue(), firstnameTf.getValue(), lastnameTf.getValue(), emailTf.getValue());
        			userCreated = userService.create(userPostDto, rolesSelected);
        		}
			} catch (UserAlreadyExistsException e) {
				Notification.show("INVALID_FORM","Username already exists", Notification.Type.HUMANIZED_MESSAGE);
				e.printStackTrace();
			} finally {
				if(userCreated != null) {
					usernameTf.clear();
					passwordTf.clear();
					passwordRepeatTf.clear();
					firstnameTf.clear();
					lastnameTf.clear();
					emailTf.clear();
					roleListSelect.clear();
				}
 				this.setEnabled(true);
 			}
         });
         
         form.addComponent(createBtn);
         
         content.addComponent(form);
         addComponent(content);
    }

	private Boolean isValidForm() {
		if(usernameTf.isEmpty()) {
			Notification.show("INVALID_FORM","Username cant be empty", Notification.Type.HUMANIZED_MESSAGE);
			return false;
		}
		if(userService.read(usernameTf.getValue()) != null) {
			Notification.show("INVALID_FORM","Username already exists", Notification.Type.HUMANIZED_MESSAGE);
			return false;
		}
		if(passwordTf.isEmpty()) {
			Notification.show("INVALID_FORM","Password cant be empty", Notification.Type.HUMANIZED_MESSAGE);
			return false;
		}
		if(passwordRepeatTf.isEmpty()) {
			Notification.show("INVALID_FORM","Password repeated cant be empty", Notification.Type.HUMANIZED_MESSAGE);
			return false;
		}
		if(!passwordTf.getValue().equals(passwordRepeatTf.getValue())) {
			Notification.show("INVALID_FORM","Passwords must be equals", Notification.Type.HUMANIZED_MESSAGE);
			return false;
		}
		if(firstnameTf.isEmpty()) {
			Notification.show("INVALID_FORM","Firstname cant be empty", Notification.Type.HUMANIZED_MESSAGE);
			return false;
		}
		if(lastnameTf.isEmpty()) {
			Notification.show("INVALID_FORM","Lastname cant be empty", Notification.Type.HUMANIZED_MESSAGE);
			return false;
		}
		if(emailTf.isEmpty()) {
			Notification.show("INVALID_FORM","Email cant be empty", Notification.Type.HUMANIZED_MESSAGE);
			return false;
		}
		if(rolesSelected.isEmpty()) {
			Notification.show("INVALID_FORM","Select any role", Notification.Type.HUMANIZED_MESSAGE);
			return false;
		}
			
		return true;
	}
	
    @Override
    public void enter(ViewChangeEvent event) {
    }
}
