package com.pd.vaadin.view;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.OrderDao;
import com.pd.dao.OrderLineDao;
import com.pd.dao.ProductDao;
import com.pd.dao.RestaurantDao;
import com.pd.dao.security.UserDao;
import com.pd.model.Order;
import com.pd.model.OrderLine;
import com.pd.model.OrderType;
import com.pd.model.Product;
import com.pd.model.Restaurant;
import com.pd.model.Zone;
import com.pd.model.security.User;
import com.pd.vaadin.Broadcaster;
import com.pd.vaadin.utils.SecurityUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = NewOrderView.VIEW_ROUTE)
public class NewOrderView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -678558853372417010L;

	private final OrderDao orderDao;

	private final ProductDao productDao;
	
	private final RestaurantDao restaurantDao;
	
	private final UserDao userDao;
	
	private final OrderLineDao orderLineDao;

	public static final String VIEW_ROUTE = "NewOrderView";
	public static final String VIEW_NAME = "NewOrderView";

	VerticalLayout options;
	Grid<OrderLine> orderList;
	Order order;
	Set<OrderLine> orderLineSet = new HashSet<OrderLine>();
	OrderLine orderline;
	Button makeOrder = new Button("Open new order");
	ListSelect<Zone> zones = new ListSelect<Zone>();

	private boolean alreadyExists;
	private FooterCell footer;
	
	private User currentUser;
	private Label labelRestaurant;
	private Restaurant currentRestaurant;
	private VerticalLayout verticalTitleLayout;
	private OrderType orderType;

	@Autowired
	public NewOrderView(OrderDao orderDao, ProductDao productDao, RestaurantDao restaurantDao, UserDao userDao, OrderLineDao orderLineDao) {
		this.orderDao = orderDao;
		this.productDao = productDao;
		this.restaurantDao = restaurantDao;
		this.userDao = userDao;
		this.orderLineDao = orderLineDao;
	}

	@PostConstruct
	void init() {
		this.setResponsive(true);
		Label header = new Label("New Order");
		header.addStyleName(ValoTheme.LABEL_H2);
		header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		verticalTitleLayout = new VerticalLayout(new HorizontalLayout(header));
		verticalTitleLayout.setMargin(new MarginInfo(false, false, false, true));
		addComponent(verticalTitleLayout);
		loadRestaurant();
		if(currentRestaurant != null) {
			
			HorizontalLayout todo = new HorizontalLayout();
			todo.setSizeFull();
			
			//OrderList
			VerticalLayout orderListLayout = new VerticalLayout();
			orderList = new Grid<OrderLine>();
			orderList.addColumn(OrderLine::getProduct).setCaption("Product name").setId("productname");
			orderList.addColumn(OrderLine::getAmount).setCaption("Amount").setId("amount");
			orderList.addColumn(OrderLine::getTotal).setCaption("Total").setId("total");
			orderList.getColumn("productname").setExpandRatio(1);
			orderList.getColumn("amount").setWidth(100);
			orderList.getColumn("total").setWidth(100);
			footer = orderList.appendFooterRow().join(
					orderList.getColumn("productname"),
					orderList.getColumn("amount"),
					orderList.getColumn("total")
					);
			orderListLayout.setSizeFull();
			orderList.setWidth(100, Unit.PERCENTAGE);
			orderList.setHeight(240, Unit.PIXELS);
			order = new Order();
			orderList.addAttachListener(event -> {
				orderList.setItems(orderLineSet);
				calculateLinesTotal();
			});
			orderList.addItemClickListener(event -> {
				OrderLine ol = event.getItem();
				if(ol != null) {
					if(ol.getAmount() > 1)
						ol.setAmount(ol.getAmount()-1);
					else {
						orderLineSet.removeIf(p->p.equals(ol));
						orderList.setItems(orderLineSet);
					}
					calculateLinesTotal();
				}
			});
			orderListLayout.addComponent(orderList);
			
			options = new VerticalLayout();
			options.setResponsive(true);
			options.setWidth(100, Unit.PERCENTAGE);
			
			makeOrder.addClickListener(event -> {
				if(orderLineSet.isEmpty())
					showNotification(new Notification("Order cant be empty!"));
				else {
					order.setLines(orderLineSet);
					order.setRestaurant(currentRestaurant);
					order.setType(orderType);
					order = orderDao.save(order);
					StringBuilder sb = new StringBuilder();
					sb.append("Order ID:" +order.getId()+"\n");
					for(OrderLine ol: orderLineSet) {
						ol.setOrderObject(order);
						orderLineDao.save(ol);
						sb.append(ol.getProduct()+", # "+ol.getAmount()+"\n");
					}
					/*try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					makeBroadcast(sb.toString());
					order = new Order();
					orderLineSet.clear();
					orderList.setItems(orderLineSet);
					calculateLinesTotal();
					showNotification(new Notification("Submitting order"));
				}
			});
			orderListLayout.addComponent(makeOrder);
			todo.addComponent(options);
			todo.addComponent(orderListLayout);
			orderList.setHeight(600, Unit.PIXELS);
			todo.setExpandRatio(options, 2.0f);
			todo.setExpandRatio(orderListLayout, 1.0f);
			addComponent(todo);

		}
	}
	
	private void makeBroadcast(/*Set<OrderLine> lines*/String msg) {
		getSession().lock();
		//System.out.println("A enviar: " +lines.toString());
		Broadcaster.broadcast(msg);
		//System.out.println("Enviado: " +lines.toString());
		getSession().unlock();
	}

	private void calculateLinesTotal() {
		Double total = orderLineSet.stream().mapToDouble(ol -> ol.getTotal()).sum();
		footer.setText("Order Total: " +total.toString());
	}
	
	private void showNotification(Notification notification) {
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }
	
	private void loadImage() {
		for (Product p : productDao.findByCanBeSoldAlone()) {
			Panel panel = new Panel();
			Image image = new Image(p.getName(), p.getImage());
			image.setStyleName(ValoTheme.BUTTON_PRIMARY);
			image.setWidth(140, Unit.PIXELS);
			image.setHeight(100, Unit.PIXELS);
			panel.addClickListener(event -> {
				alreadyExists = false;
				for (OrderLine ol : orderLineSet) {
					if (ol.getProduct().equals(p)) {
						alreadyExists = true;
						ol.setAmount(ol.getAmount() + 1);
					}
				}
				if (!alreadyExists) {
					orderLineSet.add(new OrderLine(order, p, 1));
				}
				orderList.setItems(orderLineSet);
				calculateLinesTotal();
			});
			panel.setStyleName(ValoTheme.PANEL_WELL);
			panel.setSizeUndefined();
			panel.setCaption(p.getName());
			panel.setContent(image);
			options.addComponent(panel);
		}
	}
	
	private void loadRestaurant() {
		currentUser = userDao.findByUsername(SecurityUtils.getCurrentUser());
		Button btnLocal = new Button(OrderType.LOCAL.toString());
		Button btnDeliver = new Button(OrderType.HOMEDELIVERY.toString());
		Button btnTakeAway = new Button(OrderType.TOTAKEAWAY.toString());
		if(currentUser.getUsername().equals("admin")) {
			labelRestaurant = new Label("Logued as Admin");
			labelRestaurant.addStyleName(ValoTheme.LABEL_SUCCESS);
		}else{
			currentRestaurant = restaurantDao.findByWorker(currentUser);
			if(currentRestaurant == null) {
				labelRestaurant = new Label("Current user "+ currentUser.getUsername().toUpperCase() +" doesnt work in any restaurant");
				labelRestaurant.addStyleName(ValoTheme.LABEL_FAILURE);
				//verticalTitleLayout.replaceComponent(labelRestaurant, labelRestaurant);
			}
			else {
				labelRestaurant = new Label(
						"Current user is " + currentUser.getUsername().toUpperCase() 
						+ " and current restaurant is " 
						+ currentRestaurant.toString().toUpperCase());
				labelRestaurant.addStyleName(ValoTheme.LABEL_SUCCESS);
			}
			
			HorizontalLayout hz = new HorizontalLayout(labelRestaurant);
			//Buttons to chose order type
			if(currentRestaurant != null) {
				Label currentType = new Label();
				btnLocal.addClickListener(event->{
					orderType = OrderType.LOCAL;
					currentType.setCaption("Type: " +orderType);
				});
				btnDeliver.addClickListener(event->{
					orderType = OrderType.HOMEDELIVERY;
					currentType.setCaption("Type: " +orderType);
				});
				btnTakeAway.addClickListener(event->{
					orderType = OrderType.TOTAKEAWAY;
					currentType.setCaption("Type: " +orderType);
				});
				hz.addComponent(btnLocal);
				hz.addComponent(btnDeliver);
				hz.addComponent(btnTakeAway);
				hz.addComponent(currentType);
			}
			verticalTitleLayout.addComponent(hz);
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if(currentRestaurant != null) {
			options.removeAllComponents();
			loadImage();
		}
	}

}
