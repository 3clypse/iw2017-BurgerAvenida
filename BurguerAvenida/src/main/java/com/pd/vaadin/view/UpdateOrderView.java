package com.pd.vaadin.view;

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
import com.pd.model.Order;
import com.pd.model.OrderLine;
import com.pd.model.OrderStatus;
import com.pd.model.OrderType;
import com.pd.model.Product;
import com.pd.model.Restaurant;
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
@SpringView(name = UpdateOrderView.VIEW_ROUTE)
public class UpdateOrderView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1916821205661337977L;
	
	public static final String VIEW_ROUTE = "UpdateOrderView";
	public static final String VIEW_NAME = "UpdateOrderView";
	
	private final OrderDao orderDao;

	private final ProductDao productDao;
	
	private final RestaurantDao restaurantDao;
	
	private final UserDao userDao;
	
	private final OrderLineDao orderLineDao;
	
	private final ZoneDao zoneDao;
	
	private final ClientDao clientDao;
	
	private boolean alreadyExists;
	
	Set<OrderLine> orderLineSetNewOl = new HashSet<OrderLine>();
	Set<OrderLine> orderLineSetInKitchen = new HashSet<OrderLine>();
	Grid<OrderLine> orderListInKitchen = new Grid<>();
	Grid<OrderLine> orderListNewOl = new Grid<>();
	Order currentOrder;
	
	private User currentUser;
	private Label labelRestaurant;
	private Restaurant currentRestaurant;
	private OrderType orderType;
	private ComboBox<Order> currentOrders = new ComboBox<>();
	
	private VerticalLayout verticalTitleLayout;
	private HorizontalLayout hz = new HorizontalLayout();
	private TabSheet tabsheet = new TabSheet();
	private VerticalLayout productsLayout = new VerticalLayout();
	private HorizontalLayout hzProductLayout = new HorizontalLayout();
	private VerticalLayout orderListLayout = new VerticalLayout();
	private HorizontalLayout hzOrderListLayout = new HorizontalLayout();
	private FooterCell footer;
	private FooterCell footerNewLines;
	private Button makeOrder = new Button("Update order");
	
	@Autowired
	public UpdateOrderView(OrderDao orderDao, ProductDao productDao, RestaurantDao restaurantDao, UserDao userDao, OrderLineDao orderLineDao, ZoneDao zoneDao, ClientDao clientDao) {
		this.orderDao = orderDao;
		this.productDao = productDao;
		this.restaurantDao = restaurantDao;
		this.userDao = userDao;
		this.orderLineDao = orderLineDao;
		this.zoneDao = zoneDao;
		this.clientDao = clientDao;
	}
	
	@PostConstruct
	void init() {
		this.setResponsive(true);
		Label header = new Label("Update Order");
		header.addStyleName(ValoTheme.LABEL_H2);
		header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		verticalTitleLayout = new VerticalLayout(new HorizontalLayout(header));
		verticalTitleLayout.setMargin(new MarginInfo(false, false, false, true));
		addComponent(verticalTitleLayout);
		loadRestaurant();
		tabsheet.setVisible(false);
	}
	
	private void loadImage() {
		hzProductLayout.removeAllComponents();
		productsLayout.removeAllComponents();
		for (Product p : productDao.findByCanBeSoldAlone()) {
			Panel panel = new Panel();
			Image image = new Image(p.getName(), p.getImage());
			image.setStyleName(ValoTheme.BUTTON_PRIMARY);
			image.setWidth(140, Unit.PIXELS);
			image.setHeight(100, Unit.PIXELS);
			panel.addClickListener(event -> {
				alreadyExists = false;
				for (OrderLine ol : orderLineSetNewOl) {
					if (ol.getProduct().equals(p)) {
						alreadyExists = true;
						ol.setAmount(ol.getAmount() + 1);
					}
				}
				if (!alreadyExists) {
					orderLineSetNewOl.add(new OrderLine(currentOrder, p, 1));
				}
				orderListNewOl.setItems(orderLineSetNewOl);
				calculateNewLinesTotal();
			});
			panel.setStyleName(ValoTheme.PANEL_WELL);
			panel.setSizeUndefined();
			panel.setCaption(p.getName());
			panel.setContent(image);
			
			hzProductLayout.addComponent(panel);
		}
		productsLayout.addComponent(hzProductLayout);
		tabsheet.addComponent(productsLayout);
	}
	
	private void calculateLinesTotal() {
		Double total = currentOrder.getLines().stream().mapToDouble(ol -> ol.getTotal()).sum();
		footer.setText("Order Total: " +total);
	}
	
	private void calculateNewLinesTotal() {
		Double total = orderLineSetNewOl.stream().mapToDouble(ol -> ol.getTotal()).sum();
		footerNewLines.setText("Order Total: " +total);
	}
	
	private void makeBroadcast(String msg) {
		getSession().lock();
		Broadcaster.broadcast(msg);
		getSession().unlock();
	}
	
	private void showNotification(Notification notification) {
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
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
				hzOrderListLayout.setSizeFull();
				orderListInKitchen.setCaption("Lines Already In Kitchen");
				orderListInKitchen.addColumn(OrderLine::getProduct).setCaption("Product name").setId("productname");
				orderListInKitchen.addColumn(OrderLine::getAmount).setCaption("Amount").setId("amount");
				orderListInKitchen.addColumn(OrderLine::getTotal).setCaption("Total").setId("total");
				footer = orderListInKitchen.appendFooterRow().join(
						orderListInKitchen.getColumn("productname"),
						orderListInKitchen.getColumn("amount"),
						orderListInKitchen.getColumn("total"));
				orderListInKitchen.setSizeFull();
				
				orderListNewOl.setCaption("New lines");
				orderListNewOl.addColumn(OrderLine::getProduct).setCaption("Product name").setId("productname");
				orderListNewOl.addColumn(OrderLine::getAmount).setCaption("Amount").setId("amount");
				orderListNewOl.addColumn(OrderLine::getTotal).setCaption("Total").setId("total");
				footerNewLines = orderListNewOl.appendFooterRow().join(
						orderListNewOl.getColumn("productname"),
						orderListNewOl.getColumn("amount"),
						orderListNewOl.getColumn("total"));
				orderListNewOl.setSizeFull();
				
				orderListNewOl.addAttachListener(e -> {
					orderListNewOl.setItems(orderLineSetNewOl);
				});
				
				orderListNewOl.addItemClickListener(ev -> {
					OrderLine ol = ev.getItem();
					if(ol != null) {
						if(ol.getAmount() > 1)
							ol.setAmount(ol.getAmount()-1);
						else {
							orderLineSetNewOl.removeIf(p->p.equals(ol));
							orderListNewOl.setItems(orderLineSetNewOl);
						}
						calculateNewLinesTotal();
					}
				});
				
				makeOrder.addClickListener(e -> {
					if(orderLineSetNewOl.isEmpty())
						showNotification(new Notification("Lines cant be empty!"));
					else {
						StringBuilder sb = new StringBuilder();
						sb.append(orderType.toString());
						sb.append("Order ID:" +currentOrder.getId()+"\n");
						Set<OrderLine> lines = orderLineSetNewOl;
						for(OrderLine i: currentOrder.getLines()) {
							for(OrderLine j: lines) {
								if(i.equals(j)) {
									i.setAmount(i.getAmount()+j.getAmount());
									i.calculateTotal();
									orderLineDao.save(i);
									sb.append(j.getProduct()+", # "+j.getAmount()+"\n");
									lines.remove(j);
								}
							}
						}
						currentOrder = orderDao.save(currentOrder);
						for(OrderLine ol: lines) {
							ol.setOrderObject(currentOrder);
							ol.setIsInKitchen(true);
							orderLineDao.save(ol);
							sb.append(ol.getProduct()+", # "+ol.getAmount()+"\n");
						}
						makeBroadcast(sb.toString());
						orderLineSetNewOl.clear();
						orderListNewOl.setItems(orderLineSetNewOl);
						calculateNewLinesTotal();
						showNotification(new Notification("Updating order"));
						orderLineSetNewOl.clear();
						orderListNewOl.setItems(orderLineSetNewOl);
						calculateNewLinesTotal();
						currentOrder = orderDao.findOne(currentOrders.getSelectedItem().get().getId());
						orderListInKitchen.setItems(currentOrder.getLines());
						calculateLinesTotal();
						
						hzOrderListLayout.addComponent(orderListInKitchen);
						hzOrderListLayout.addComponent(orderListNewOl);
						orderListLayout.addComponent(hzOrderListLayout);
						loadImage();
						tabsheet.setVisible(true);
						
					}
				});
				orderListLayout.addComponent(makeOrder);
				
				Label currentType = new Label();
				btnLocal.addClickListener(event->{
					orderType = OrderType.LOCAL;
					btnLocal.setEnabled(false);
					btnDeliver.setEnabled(false);
					btnTakeAway.setEnabled(false);
					
					btnDeliver.setVisible(false);
					btnTakeAway.setVisible(false);
					
					currentOrders.setItems(orderDao.findByStatusAndType(OrderStatus.OPENED, OrderType.LOCAL, currentRestaurant));
					currentOrders.setItemCaptionGenerator(o -> o.getZone().getDescription()+", ID: "+o.getId()+", "+o.getCreatedAt());
					currentOrders.setEmptySelectionAllowed(false);
					currentOrders.setWidth(400, Unit.PIXELS);
					hz.addComponent(new Label("Order: "));
					hz.addComponent(currentOrders);
					
					currentOrders.addSelectionListener(e -> {
						orderLineSetNewOl.clear();
						orderListNewOl.setItems(orderLineSetNewOl);
						calculateNewLinesTotal();
						currentOrder = orderDao.findOne(e.getSelectedItem().get().getId());
						orderListInKitchen.setItems(currentOrder.getLines());
						calculateLinesTotal();
						
						hzOrderListLayout.addComponent(orderListInKitchen);
						hzOrderListLayout.addComponent(orderListNewOl);
						orderListLayout.addComponent(hzOrderListLayout);
						loadImage();
						tabsheet.setVisible(true);
					});
					
					productsLayout.setCaption("Products");
					orderListLayout.setCaption("Order List");
					tabsheet.addComponent(orderListLayout);
					addComponent(tabsheet);
				});
				btnDeliver.addClickListener(event->{
					orderType = OrderType.HOMEDELIVERY;
					btnLocal.setEnabled(false);
					btnDeliver.setEnabled(false);
					btnTakeAway.setEnabled(false);
					
					btnLocal.setVisible(false);
					btnTakeAway.setVisible(false);
					
					currentOrders.setItems(orderDao.findByStatusAndType(OrderStatus.OPENED, OrderType.HOMEDELIVERY, currentRestaurant));
					currentOrders.setItemCaptionGenerator(o -> 
						 o.getClient().getName()+", "
						+o.getClient().getAddress()+", "
						+o.getClient().getPhoneNumber()+", ID: "+o.getId()
						+", "+o.getCreatedAt());
					currentOrders.setEmptySelectionAllowed(false);
					currentOrders.setWidth(600, Unit.PIXELS);
					hz.addComponent(new Label("Order: "));
					hz.addComponent(currentOrders);
					
					currentOrders.addSelectionListener(e -> {
						orderLineSetNewOl.clear();
						orderListNewOl.setItems(orderLineSetNewOl);
						calculateNewLinesTotal();
						currentOrder = orderDao.findOne(e.getSelectedItem().get().getId());
						orderListInKitchen.setItems(currentOrder.getLines());
						calculateLinesTotal();
						
						hzOrderListLayout.addComponent(orderListInKitchen);
						hzOrderListLayout.addComponent(orderListNewOl);
						orderListLayout.addComponent(hzOrderListLayout);
						loadImage();
						tabsheet.setVisible(true);
					});
					
					productsLayout.setCaption("Products");
					orderListLayout.setCaption("Order List");
					tabsheet.addComponent(orderListLayout);
					addComponent(tabsheet);
				});
				btnTakeAway.addClickListener(event->{
					orderType = OrderType.TOTAKEAWAY;
					btnLocal.setEnabled(false);
					btnDeliver.setEnabled(false);
					btnTakeAway.setEnabled(false);
					
					btnLocal.setVisible(false);
					btnDeliver.setVisible(false);
					
					currentOrders.setItems(orderDao.findByStatusAndType(OrderStatus.OPENED, OrderType.TOTAKEAWAY, currentRestaurant));
					currentOrders.setItemCaptionGenerator(o -> 
						 o.getClient().getName()+", "
						+o.getClient().getAddress()+", "
						+o.getClient().getPhoneNumber()+", ID: "+o.getId()
						+", "+o.getCreatedAt());
					currentOrders.setEmptySelectionAllowed(false);
					currentOrders.setWidth(600, Unit.PIXELS);
					hz.addComponent(new Label("Order: "));
					hz.addComponent(currentOrders);
					
					currentOrders.addSelectionListener(e -> {
						orderLineSetNewOl.clear();
						orderListNewOl.setItems(orderLineSetNewOl);
						calculateNewLinesTotal();
						currentOrder = orderDao.findOne(e.getSelectedItem().get().getId());
						orderListInKitchen.setItems(currentOrder.getLines());
						calculateLinesTotal();
						
						hzOrderListLayout.addComponent(orderListInKitchen);
						hzOrderListLayout.addComponent(orderListNewOl);
						orderListLayout.addComponent(hzOrderListLayout);
						loadImage();
						tabsheet.setVisible(true);
					});
					
					productsLayout.setCaption("Products");
					orderListLayout.setCaption("Order List");
					tabsheet.addComponent(orderListLayout);
					addComponent(tabsheet);
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
		// TODO Auto-generated method stub
		
	}

}
