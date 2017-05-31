package com.pd.vaadin.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.pd.dao.CheckoutDao;
import com.pd.dao.OrderDao;
import com.pd.dao.RestaurantDao;
import com.pd.dao.security.UserDao;
import com.pd.model.Checkout;
import com.pd.model.Order;
import com.pd.model.OrderStatus;
import com.pd.model.Restaurant;
import com.pd.model.security.User;
import com.pd.vaadin.utils.SecurityUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Scope("prototype")
@SpringView(name = CheckoutView.VIEW_ROUTE)
public class CheckoutView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8630196551297678294L;
	
	public static final String VIEW_ROUTE = "CheckoutView";
	public static final String VIEW_NAME = "CheckoutView";
	
	private final OrderDao orderDao;
	
	private final RestaurantDao restaurantDao;
	
	private final UserDao userDao;
	
	private final CheckoutDao checkoutDao;
	
	private User currentUser;
	private Label labelRestaurant;
	private Restaurant currentRestaurant;
	private ComboBox<String> avaliableDates = new ComboBox<>();
	private List<Order> orders = new ArrayList<Order>();
	private Grid<String> ckList = new Grid<>();
	private Set<String> ckListSet = new HashSet<String>();
	
	private VerticalLayout content = new VerticalLayout();
	private VerticalLayout verticalTitleLayout;
	private HorizontalLayout hz = new HorizontalLayout();
	private Button checkout = new Button("Checkout");
	private Checkout ck;

	@Autowired
	public CheckoutView(OrderDao orderDao, RestaurantDao restaurantDao, UserDao userDao, CheckoutDao checkoutDao) {
		this.orderDao = orderDao;
		this.restaurantDao = restaurantDao;
		this.userDao = userDao;
		this.checkoutDao = checkoutDao;
	}
	
	@PostConstruct
	void init() {
		this.setResponsive(true);
		Label header = new Label("Checkout");
		header.addStyleName(ValoTheme.LABEL_H2);
		header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		verticalTitleLayout = new VerticalLayout(new HorizontalLayout(header));
		verticalTitleLayout.setMargin(new MarginInfo(false, false, false, true));
		addComponent(verticalTitleLayout);
		loadRestaurant();
		checkout.setEnabled(false);
		content.setSizeFull();
		ckList.setSizeFull();
		ckList.addColumn(String::toString).setCaption("Checkouts");
		content.addComponent(ckList);
		setCheckouts();
		addComponent(content);
	}
	
	private void setCheckouts() {
		ckListSet.clear();
		for(Checkout c: checkoutDao.findAll()) {
			ckListSet.add(c.toString());
		}
		ckList.setItems(ckListSet);
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

			hz = new HorizontalLayout(labelRestaurant);
			// Buttons to chose order type
			if (currentRestaurant != null) {
				Label lb = new Label("Select date");
				avaliableDates.setItems(Lists.newArrayList(orderDao.findAllDates()));
				
				checkout.addClickListener(e-> {
					orders = orderDao.findByDate(avaliableDates.getValue(), OrderStatus.CLOSED);
					Double total = orders.stream().mapToDouble(o -> o.getTotal()).sum();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date parseDate = null;
					try {
						parseDate = sdf.parse(avaliableDates.getValue());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					if(checkoutDao.findByDate(avaliableDates.getValue()) != null) {
						ck = checkoutDao.findByDate(avaliableDates.getValue());
						ck.setTotal(total);
						ck.setOrders(orders.size());
					} else {
						ck = new Checkout(parseDate, orders.size(), total, currentRestaurant);
					}
					checkoutDao.save(ck);
					setCheckouts();
				});
				
				avaliableDates.setEmptySelectionAllowed(false);
				avaliableDates.addSelectionListener(e-> {
					checkout.setEnabled(true);
				});
				
				hz.addComponent(lb);
				hz.addComponent(avaliableDates);
				hz.addComponent(checkout);
			}
			verticalTitleLayout.addComponent(hz);
		}
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
