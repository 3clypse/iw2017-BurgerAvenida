package com.pd.vaadin.view.productfamily;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.ProductFamilyDao;
import com.pd.model.Client;
import com.pd.model.ProductFamily;
import com.vaadin.data.Binder;
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
public class ProductFamilyEditor extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1700349086596241763L;

	
	private final ProductFamilyDao repository;

	/**
	 * The currently object
	 */
	private ProductFamily currentObject;

	TextField name = new TextField("Name");

	Button save = new Button("Save", VaadinIcons.SAFE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcons.ERASER);
	CssLayout actions = new CssLayout(save, cancel, delete);

	Binder<ProductFamily> binder = new Binder<>(ProductFamily.class);

	@Autowired
	public ProductFamilyEditor(ProductFamilyDao repository) {
		this.repository = repository;
		
		name.setSizeFull();
		name.setMaxLength(32);
		
		addComponents(name, actions);

		binder.bindInstanceFields(this);
		
		binder.forField(name)
		.asRequired("Cant be empty")
	    .withValidator(new StringLengthValidator(
	        "Name must be between 2 and 32 characters long",
	        2, 32))
	    .bind(ProductFamily::getName, ProductFamily::setName);

		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

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

	public final void edit(ProductFamily c) {
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
	
	private void showNotification(Notification notification) {
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }
	
}