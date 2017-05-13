package com.pd.vaadin.view.order;

import java.util.Collection;
import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.ClientDao;
import com.pd.dao.OrderDao;
import com.pd.dao.ZoneDao;
import com.pd.model.Client;
import com.pd.model.Order;
import com.pd.model.OrderStatus;
import com.pd.model.OrderType;
import com.pd.model.Zone;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringComponent
public class OrderEditor extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1700349086596241763L;

	
	private final OrderDao repository;
	
	@SuppressWarnings("unused")
	private final ZoneDao zoneDao;
	
	@SuppressWarnings("unused")
	private final ClientDao clientDao;
	
	/**
	 * The currently object
	 */
	private Order currentObject;

	DateField createdAt = new DateField("Created At");
	DateField closedAt = new DateField("Closed At");
	ComboBox<OrderStatus> status = new ComboBox<OrderStatus>("Status");
	ComboBox<OrderType> type = new ComboBox<OrderType>("Order Type");
	ComboBox<Zone> zone = new ComboBox<Zone>("Zone");
	ComboBox<Client> client = new ComboBox<Client>("Client");

	Button save = new Button("Save", VaadinIcons.SAFE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcons.ERASER);
	CssLayout actions = new CssLayout(save, cancel, delete);

	Binder<Order> binder;
	
	@Autowired
	public OrderEditor(OrderDao repository, ZoneDao zoneDao, ClientDao clientDao) {
		this.repository = repository;
		this.zoneDao = zoneDao;
		this.clientDao = clientDao;

		createdAt.setEnabled(false);
		closedAt.setEnabled(false);
		status.setEnabled(false);
		zone.setEmptySelectionAllowed(false);
		
		status.setItems(EnumSet.allOf(OrderStatus.class));
		type.setItems(EnumSet.allOf(OrderType.class));
		zone.setItems((Collection<Zone>) zoneDao.findAll());
		client.setItems((Collection<Client>) clientDao.findAll());
		addComponents(createdAt, closedAt, status, type, zone, client, actions);
		
		binder = new BeanValidationBinder<>(Order.class);
		binder.forMemberField(createdAt).withConverter(new LocalDateToDateConverter());
		binder.forMemberField(closedAt).withConverter(new LocalDateToDateConverter());
		zone.setItemCaptionGenerator(zone -> zone.getDescription()+" of Restaurant "+zone.getRestaurant().getName());
		binder.bindInstanceFields(this);

		createdAt.setSizeFull();
		closedAt.setSizeFull();
		status.setSizeFull();
		type.setSizeFull();
		zone.setSizeFull();
		client.setSizeFull();
		
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

	public final void edit(Order c) {
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