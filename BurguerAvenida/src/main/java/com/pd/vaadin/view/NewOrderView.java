package com.pd.vaadin.view;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.pd.dao.ClientDao;
import com.pd.dao.OrderDao;
import com.pd.dao.OrderLineDao;
import com.pd.dao.ProductDao;
import com.pd.dao.RestaurantDao;
import com.pd.dao.ZoneDao;
import com.pd.dao.security.UserDao;
import com.pd.model.Client;
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
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.themes.ValoTheme;

@Scope("prototype")
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
	
	private final ZoneDao zoneDao;
	
	private final ClientDao clientDao;

	public static final String VIEW_ROUTE = "NewOrderView";
	public static final String VIEW_NAME = "NewOrderView";

	Grid<OrderLine> orderList;
	Order order;
	Set<OrderLine> orderLineSet = new HashSet<OrderLine>();
	OrderLine orderline;
	Button makeOrder = new Button("Open new order");

	private boolean alreadyExists;
	private FooterCell footer;
	
	private User currentUser;
	private Label labelRestaurant;
	private Restaurant currentRestaurant;
	private VerticalLayout verticalTitleLayout;
	private OrderType orderType;
	private ComboBox<Zone> zones = new ComboBox<>();
	private ComboBox<Client> clients = new ComboBox<>();
	
	private HorizontalLayout hz;
	private VerticalLayout productsLayout = new VerticalLayout();
	private HorizontalLayout hzProductLayout = new HorizontalLayout();
	private VerticalLayout orderListLayout = new VerticalLayout();
	private TabSheet tabsheet = new TabSheet();

	@Autowired
	public NewOrderView(OrderDao orderDao, ProductDao productDao, RestaurantDao restaurantDao, UserDao userDao, OrderLineDao orderLineDao, ZoneDao zoneDao, ClientDao clientDao) {
		this.orderDao = orderDao;
		this.productDao = productDao;
		this.restaurantDao = restaurantDao;
		this.userDao = userDao;
		this.orderLineDao = orderLineDao;
		this.zoneDao = zoneDao;
		this.clientDao = clientDao;
	}
	
	private void loadLocalOrder() {
		zones.setItems(zoneDao.findByRestaurant(currentRestaurant));
		zones.setItemCaptionGenerator(zone -> zone.getDescription());
		zones.setEmptySelectionAllowed(false);
		zones.setWidth(300, Unit.PIXELS);
		hz.addComponent(new Label("Zone: "));
		hz.addComponent(zones);
		
		productsLayout.setCaption("Products");
		orderListLayout.setCaption("Order List");
		
		loadImage();
		buildOrderlist();
		
		tabsheet.addComponent(productsLayout);
		tabsheet.addComponent(orderListLayout);
		
		addComponent(tabsheet);
	}
	
	private void loadHomeOrder() {
		clients.setItems((Collection<Client>) clientDao.findAll());
		clients.setItemCaptionGenerator(c -> c.getName()+", "+c.getAddress()+", "+c.getPhoneNumber());
		clients.setEmptySelectionAllowed(false);
		clients.setWidth(400, Unit.PIXELS);
		hz.addComponent(new Label("Client: "));
		hz.addComponent(clients);
		
		productsLayout.setCaption("Products");
		orderListLayout.setCaption("Order List");
		
		loadImage();
		buildOrderlist();
		
		tabsheet.addComponent(productsLayout);
		tabsheet.addComponent(orderListLayout);
		
		addComponent(tabsheet);
	}
	
	private void loadAwayOrder() {
		clients.setItems((Collection<Client>) clientDao.findAll());
		clients.setItemCaptionGenerator(c -> c.getName()+", "+c.getAddress()+", "+c.getPhoneNumber());
		clients.setEmptySelectionAllowed(false);
		clients.setWidth(400, Unit.PIXELS);
		hz.addComponent(new Label("Client: "));
		hz.addComponent(clients);
		
		productsLayout.setCaption("Products");
		orderListLayout.setCaption("Order List");
		
		loadImage();
		buildOrderlist();
		
		tabsheet.addComponent(productsLayout);
		tabsheet.addComponent(orderListLayout);
		
		addComponent(tabsheet);
	}

	private void buildOrderlist() {
		orderList = new Grid<OrderLine>();
		orderList.addColumn(OrderLine::getProduct).setCaption("Product name").setId("productname");
		orderList.addColumn(OrderLine::getAmount).setCaption("Amount").setId("amount");
		orderList.addColumn(OrderLine::getTotal).setCaption("Total").setId("total");
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
		
		makeOrder.addClickListener(event -> {
			if(orderLineSet.isEmpty())
				showNotification(new Notification("Order cant be empty!"));
			else if (orderType == OrderType.LOCAL && !zones.getSelectedItem().isPresent()) {
					showNotification(new Notification("Zone cant be empty!"));
			}
			else if (orderType == OrderType.HOMEDELIVERY && !clients.getSelectedItem().isPresent()) {
					showNotification(new Notification("Client cant be empty!"));
			}
			else if (orderType == OrderType.TOTAKEAWAY && !clients.getSelectedItem().isPresent()) {
					showNotification(new Notification("Client cant be empty!"));
			} else {
				order.setLines(orderLineSet);
				order.setRestaurant(currentRestaurant);
				order.setType(orderType);
				if(orderType.equals(OrderType.LOCAL)) {
					order.setZone(zones.getSelectedItem().get());
				}
				if(orderType.equals(OrderType.HOMEDELIVERY)) {
					order.setClient(clients.getSelectedItem().get());
				}
				if(orderType.equals(OrderType.TOTAKEAWAY)) {
					order.setClient(clients.getSelectedItem().get());
				}
				order = orderDao.save(order);
				StringBuilder sb = new StringBuilder();
				sb.append(orderType.toString());
				sb.append("Order ID:" +order.getId()+"\n");
				for(OrderLine ol: orderLineSet) {
					ol.setOrderObject(order);
					ol.setIsInKitchen(true);
					orderLineDao.save(ol);
					sb.append(ol.getProduct()+", # "+ol.getAmount()+"\n");
				}
				makeBroadcast(sb.toString());
				order = new Order();
				orderLineSet.clear();
				orderList.setItems(orderLineSet);
				calculateLinesTotal();
				showNotification(new Notification("Submitting order"));
			}
		});
		orderListLayout.addComponent(makeOrder);
		orderList.setHeight(600, Unit.PIXELS);
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
		
	}
	
	private void makeBroadcast(String msg) {
		getSession().lock();
		Broadcaster.broadcast(msg);
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
			
			hzProductLayout.addComponent(panel);
		}
		productsLayout.addComponent(hzProductLayout);
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
			}
			else {
				labelRestaurant = new Label(
						"Current user is " + currentUser.getUsername().toUpperCase() 
						+ " and current restaurant is " 
						+ currentRestaurant.toString().toUpperCase());
				labelRestaurant.addStyleName(ValoTheme.LABEL_SUCCESS);
			}
			
			hz = new HorizontalLayout(labelRestaurant);
			//Buttons to chose order type
			if(currentRestaurant != null) {
				Label currentType = new Label();
				btnLocal.addClickListener(event->{
					orderType = OrderType.LOCAL;
					btnLocal.setEnabled(false);
					btnDeliver.setEnabled(false);
					btnTakeAway.setEnabled(false);
					
					btnDeliver.setVisible(false);
					btnTakeAway.setVisible(false);
					
					loadLocalOrder();
				});
				btnDeliver.addClickListener(event->{
					orderType = OrderType.HOMEDELIVERY;
					btnLocal.setEnabled(false);
					btnDeliver.setEnabled(false);
					btnTakeAway.setEnabled(false);
					
					btnLocal.setVisible(false);
					btnTakeAway.setVisible(false);
					
					loadHomeOrder();
				});
				btnTakeAway.addClickListener(event->{
					orderType = OrderType.TOTAKEAWAY;
					btnLocal.setEnabled(false);
					btnDeliver.setEnabled(false);
					btnTakeAway.setEnabled(false);
					
					btnLocal.setVisible(false);
					btnDeliver.setVisible(false);
					
					loadAwayOrder();
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
		
	}

}
