package com.pd.vaadin.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.OrderDao;
import com.pd.dao.OrderLineDao;
import com.pd.dao.RestaurantDao;
import com.pd.dao.security.UserDao;
import com.pd.model.Order;
import com.pd.model.OrderStatus;
import com.pd.model.OrderType;
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

	public static final String VIEW_ROUTE = "Home";
	public static final String VIEW_NAME = "Home";

	private final RestaurantDao restaurantDao;

	private final UserDao userDao;

	private final OrderDao orderDao;

	private final OrderLineDao orderLineDao;
	
	private boolean ordersLayoutBoolean;

	private User currentUser;
	private Label labelRestaurant;
	private Restaurant currentRestaurant;
	private VerticalLayout verticalTitleLayout;

	public static Grid<String> orderList = new Grid<>();
	public static Set<String> orderLineSet = new HashSet<String>();
	
	HorizontalLayout ordersLayout = new HorizontalLayout();
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
		this.setResponsive(true);
		Label header = new Label("Current Orders");
		header.addStyleName(ValoTheme.LABEL_H2);
		header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		verticalTitleLayout = new VerticalLayout(new HorizontalLayout(header));
		verticalTitleLayout.setMargin(new MarginInfo(false, false, false, true));
		addComponent(verticalTitleLayout);
		loadRestaurant();
		// orderLineSet = new
		// HashSet<OrderLine>(Lists.newArrayList(orderLineDao.findAll()));
		// System.out.println(orderLineSet.size());
		// orderList.setItems(orderLineSet);
		if(currentRestaurant != null) {
			orderList = new Grid<String>();
			orderList.setWidth(100, Unit.PERCENTAGE);
			orderList.setHeight(600, Unit.PIXELS);
			orderList.addAttachListener(event -> {
				orderList.setItems(orderLineSet);
			});
			orderList.addColumn(String::toString).setCaption("OrderLine").setId("orderline");
			//orderList.addColumn(OrderLine::getAmount).setCaption("Amount").setId("amount");
			//orderList.addColumn(OrderLine::getTotal).setCaption("Total").setId("total");
			//orderList.getColumn("productname").setExpandRatio(1);
			//orderList.getColumn("amount").setWidth(100);
			//orderList.getColumn("total").setWidth(100);
			addComponent(orderList);
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

	private void setOrdersLayout() {
		ordersLayoutBoolean = false;
		totakeaway.setCaption("To take away orders");
		homedelivery.setCaption("Home delivery orders");
		local.setCaption("Local orders");
		totakeaway.setSizeFull();
		homedelivery.setSizeFull();
		local.setSizeFull();
		ordersLayout.setSizeFull();
		ordersLayout.addComponent(totakeaway);
		ordersLayout.addComponent(homedelivery);
		ordersLayout.addComponent(local);
		// Panel for local
		VerticalLayout hlLocal = new VerticalLayout();
		List<Order> currentOpenedOrders = orderDao.findByOpenStatus(OrderStatus.OPENED, currentRestaurant);
		currentOpenedOrders.stream().filter(o -> o.getType() == OrderType.LOCAL).forEach(o -> {
			StringBuilder str = new StringBuilder();
			//System.out.println(o.getLines().size());
			o.getLines().forEach(l -> {
				str.append(l.getProduct().getName());
				str.append(", " + l.getAmount() + " units");
			});
			Label lb = new Label("id: " + str.toString());
			hlLocal.addComponent(lb);
		});
		local.setContent(hlLocal);
		// Panel for take away
		VerticalLayout hlAway = new VerticalLayout();
		currentOpenedOrders.stream().filter(o -> o.getType() == OrderType.TOTAKEAWAY).forEach(o -> {
			Label lb = new Label(o.getId().toString());
			hlAway.addComponent(lb);
		});
		totakeaway.setContent(hlAway);
		// Panel for delivery
		VerticalLayout hlDelivery = new VerticalLayout();
		currentOpenedOrders.stream().filter(o -> o.getType() == OrderType.HOMEDELIVERY).forEach(o -> {
			Label lb = new Label(o.getId().toString());
			hlDelivery.addComponent(lb);
		});
		homedelivery.setContent(hlDelivery);
		addComponent(ordersLayout);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (currentRestaurant != null) {
			ordersLayout.removeAllComponents();
			setOrdersLayout();
			//System.out.println(orderLineSet.size());
		}
	}

}