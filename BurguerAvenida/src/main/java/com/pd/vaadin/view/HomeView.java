package com.pd.vaadin.view;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.OrderDao;
import com.pd.dao.OrderLineDao;
import com.pd.dao.RestaurantDao;
import com.pd.dao.security.UserDao;
import com.pd.model.Restaurant;
import com.pd.model.security.User;
import com.pd.vaadin.utils.SecurityUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@ViewScope
@SpringView(name = HomeView.VIEW_ROUTE)
public class HomeView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6053541745106875920L;

	public static final String VIEW_ROUTE = "Kitchen";
	public static final String VIEW_NAME = "Kitchen";

	private final RestaurantDao restaurantDao;

	private final UserDao userDao;

	private final OrderDao orderDao;

	private final OrderLineDao orderLineDao;
	
	private boolean ordersLayoutBoolean;

	private User currentUser;
	private Label labelRestaurant;
	private Restaurant currentRestaurant;
	private VerticalLayout verticalTitleLayout;

	public static Grid<String> orderListLocal = new Grid<>();
	public static Set<String> orderLineSetLocal = new HashSet<String>();
	
	public static Grid<String> orderListHome = new Grid<>();
	public static Set<String> orderLineSetHome = new HashSet<String>();
	
	public static Grid<String> orderListAway = new Grid<>();
	public static Set<String> orderLineSetAway = new HashSet<String>();
	
	VerticalLayout ordersLayout = new VerticalLayout();
	Panel totakeaway = new Panel();
	Panel homedelivery = new Panel();
	Panel local = new Panel();

	@Autowired
	public HomeView(RestaurantDao restaurantDao, UserDao userDao, OrderDao orderDao, OrderLineDao orderLineDao) {
		this.restaurantDao = restaurantDao;
		this.userDao = userDao;
		this.orderDao = orderDao;
		this.orderLineDao = orderLineDao;
	}

	@PostConstruct
	void init() {
		ordersLayoutBoolean = true;
		ordersLayout.setResponsive(true);
		this.setResponsive(true);
		Label header = new Label("Current Orders");
		header.addStyleName(ValoTheme.LABEL_H2);
		header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		verticalTitleLayout = new VerticalLayout(new HorizontalLayout(header));
		verticalTitleLayout.setMargin(new MarginInfo(false, false, false, true));
		addComponent(verticalTitleLayout);
		loadRestaurant();
		if(currentRestaurant != null) {
			//Grid for local orders
			orderListLocal = new Grid<String>();
			orderListLocal.setSizeFull();
			orderListLocal.setHeight(240, Unit.PIXELS);
			orderListLocal.addAttachListener(event -> {
				orderListLocal.setItems(orderLineSetLocal);
			});
			orderListLocal.addItemClickListener(event -> {
				String str = event.getItem();
				orderLineSetLocal.removeIf(p->p.equals(str));
				orderListLocal.setItems(orderLineSetLocal);
			});
			orderListLocal.addColumn(String::toString).setCaption("LOCAL").setId("orderlinelocal");
			ordersLayout.addComponent(orderListLocal);
			//Grid for homedelivery orders
			orderListHome = new Grid<String>();
			orderListHome.setSizeFull();
			orderListHome.setHeight(240, Unit.PIXELS);
			orderListHome.addAttachListener(event -> {
				orderListHome.setItems(orderLineSetHome);
			});
			orderListHome.addItemClickListener(event -> {
				String str = event.getItem();
				orderLineSetHome.removeIf(p->p.equals(str));
				orderListHome.setItems(orderLineSetHome);
			});
			orderListHome.addColumn(String::toString).setCaption("HOMEDELIVERY").setId("orderlinehome");
			ordersLayout.addComponent(orderListHome);
			//Grid for takeaway orders
			orderListAway = new Grid<String>();
			orderListAway.setSizeFull();
			orderListAway.setHeight(240, Unit.PIXELS);
			orderListAway.addAttachListener(event -> {
				orderListAway.setItems(orderLineSetAway);
			});
			orderListAway.addItemClickListener(event -> {
				String str = event.getItem();
				orderLineSetAway.removeIf(p->p.equals(str));
				orderListAway.setItems(orderLineSetAway);
			});
			orderListAway.addColumn(String::toString).setCaption("TAKEAWAY").setId("orderlineaway");
			ordersLayout.addComponent(orderListAway);
			
			addComponent(ordersLayout);
		}
		
	}
	
	private void loadRestaurant() {
		currentUser = userDao.findByUsername(SecurityUtils.getCurrentUser());
		if (currentUser.getUsername().equals("admin")) {
			labelRestaurant = new Label("Logued as Admin");
			labelRestaurant.addStyleName(ValoTheme.LABEL_SUCCESS);
		} else {
			currentRestaurant = restaurantDao.findByWorker(currentUser);
			if (currentRestaurant == null) {
				labelRestaurant = new Label(
						"Current user " + currentUser.getUsername().toUpperCase() + " doesnt work in any restaurant");
				labelRestaurant.addStyleName(ValoTheme.LABEL_FAILURE);
			} else {
				labelRestaurant = new Label("Current user is " + currentUser.getUsername().toUpperCase()
						+ " and current restaurant is " + currentRestaurant.toString().toUpperCase());
				labelRestaurant.addStyleName(ValoTheme.LABEL_SUCCESS);
			}
		}
		verticalTitleLayout.addComponent(new HorizontalLayout(labelRestaurant));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}