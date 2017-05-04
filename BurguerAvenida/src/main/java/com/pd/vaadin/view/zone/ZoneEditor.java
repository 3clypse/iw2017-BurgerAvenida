package com.pd.vaadin.view.zone;

import java.util.Collection;
import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.RestaurantDao;
import com.pd.dao.ZoneDao;
import com.pd.model.Restaurant;
import com.pd.model.Zone;
import com.pd.model.ZoneType;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringComponent
public class ZoneEditor extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6594278551223986980L;
	
	private final ZoneDao repository;
	
	@SuppressWarnings("unused")
	private final RestaurantDao restaurantDao;
	
	/**
	 * The currently object
	 */
	private Zone currentObject;

	ComboBox<ZoneType> zonetype = new ComboBox<>("Zone type");
	ComboBox<Restaurant> restaurants = new ComboBox<>("Restaurants");
	TextField description = new TextField("Description");

	Button save = new Button("Save", VaadinIcons.SAFE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcons.ERASER);
	CssLayout actions = new CssLayout(save, cancel, delete);

	Binder<Zone> binder;

	@Autowired
	public ZoneEditor(ZoneDao repository, RestaurantDao restaurantDao) {
		this.repository = repository;
		this.restaurantDao = restaurantDao;
		
		description.setSizeFull();
		restaurants.setSizeFull();
		zonetype.setSizeFull();
		
		zonetype.setItems(EnumSet.allOf(ZoneType.class));
		restaurants.setItems((Collection<Restaurant>) restaurantDao.findAll());
		zonetype.setEmptySelectionAllowed(false);
		restaurants.setEmptySelectionAllowed(false);
		restaurants.setItemCaptionGenerator(r-> r.getName()+", "+r.getAddress());
		addComponents(zonetype, restaurants, description, actions);
		
		binder = new BeanValidationBinder<>(Zone.class);
		binder.forField(restaurants).bind("restaurant");
		binder.forField(zonetype).bind("type");
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

	@FunctionalInterface
	public interface ChangeHandler {
		void onChange();
	}

	public final void edit(Zone c) {
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
	}

	public void setChangeHandler(ChangeHandler h) {
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}
	
}
