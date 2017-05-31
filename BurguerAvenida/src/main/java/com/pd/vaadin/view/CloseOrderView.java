package com.pd.vaadin.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
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
import com.pd.model.Restaurant;
import com.pd.model.security.User;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.themes.ValoTheme;

@Scope("prototype")
@SpringView(name = CloseOrderView.VIEW_ROUTE)
public class CloseOrderView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8521467936052410664L;

	public static final String VIEW_ROUTE = "CloseOrderView";
	public static final String VIEW_NAME = "CloseOrderView";

	private Order currentOrder;
	private User currentUser;
	private Label labelRestaurant;
	private Restaurant currentRestaurant;
	private VerticalLayout verticalTitleLayout;
	private OrderType orderType;
	private ComboBox<Order> currentOrders = new ComboBox<>();
	Set<OrderLine> orderLines = new HashSet<OrderLine>();
	Grid<OrderLine> orderList = new Grid<>("Closed Orders");
	private FooterCell footer;
	private Double total;

	private final OrderDao orderDao;

	private final ProductDao productDao;

	private final RestaurantDao restaurantDao;

	private final UserDao userDao;

	private final OrderLineDao orderLineDao;

	private final ZoneDao zoneDao;

	private final ClientDao clientDao;

	private HorizontalLayout hz;

	private VerticalLayout orderListLayout = new VerticalLayout();
	private HorizontalLayout hzOrderListLayout = new HorizontalLayout();
	private VerticalLayout options = new VerticalLayout();
	private Button closeOrderBtn = new Button("Close Order");

	@Autowired
	public CloseOrderView(OrderDao orderDao, ProductDao productDao, RestaurantDao restaurantDao, UserDao userDao,
			OrderLineDao orderLineDao, ZoneDao zoneDao, ClientDao clientDao) {
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
		Label header = new Label("Close Order");
		header.addStyleName(ValoTheme.LABEL_H2);
		header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		verticalTitleLayout = new VerticalLayout(new HorizontalLayout(header));
		verticalTitleLayout.setMargin(new MarginInfo(false, false, false, true));
		addComponent(verticalTitleLayout);
		loadRestaurant();

	}

	private void calculateLinesTotal() {
		total = currentOrder.getLines().stream().mapToDouble(ol -> ol.getTotal()).sum();
		footer.setText("Order Total: " + total);
	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
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
				orderList.setCaption("Total lines in order");
				orderList.addColumn(OrderLine::getProduct).setCaption("Product name").setId("productname");
				orderList.addColumn(OrderLine::getAmount).setCaption("Amount").setId("amount");
				orderList.addColumn(OrderLine::getTotal).setCaption("Total").setId("total");
				footer = orderList.appendFooterRow().join(orderList.getColumn("productname"),
						orderList.getColumn("amount"), orderList.getColumn("total"));
				orderList.setSizeFull();

				closeOrderBtn.addClickListener(e -> {
					Window subWindow = new Window("Close Order");
					subWindow.setWidth(260, Unit.PIXELS);
					subWindow.setHeight(360, Unit.PIXELS);
					VerticalLayout closeLayout = new VerticalLayout();
					closeLayout.setCaption("Pay order");
					VerticalLayout cancelLayout = new VerticalLayout();
					cancelLayout.setCaption("Cancel order");
					TabSheet tab = new TabSheet();
					tab.addComponent(closeLayout);
					tab.addComponent(cancelLayout);
					subWindow.setContent(tab);

					TextField giveBackMoney = new TextField("Money to give back");
					giveBackMoney.setEnabled(false);

					TextField clientMoney = new TextField("Client money");
					clientMoney.addValueChangeListener(ev -> {
						if (isNumeric(clientMoney.getValue())) {
							Double db = BigDecimal.valueOf(Double.parseDouble(clientMoney.getValue()) - total)
									.setScale(3, RoundingMode.HALF_UP).doubleValue();
							giveBackMoney.setValue(db.toString());

						} else {
							showNotification(new Notification("Only numeric values"));
						}
					});

					Button closeBtn = new Button("Close");
					closeBtn.addClickListener(ev -> {
						currentOrder.setStatus(OrderStatus.CLOSED);
						currentOrder
								.setTotal(BigDecimal.valueOf(total).setScale(3, RoundingMode.HALF_UP).doubleValue());
						currentOrder.setClosedAt(new Date());
						orderDao.save(currentOrder);
						subWindow.close();
						getUI().getNavigator().navigateTo(CloseOrderView.VIEW_NAME);
					});

					closeLayout.addComponent(clientMoney);
					closeLayout.addComponent(giveBackMoney);
					closeLayout.addComponent(closeBtn);

					cancelLayout.addComponent(new Label("Are you sure?"));
					Button cancelBtn = new Button("Cancel");
					cancelBtn.addClickListener(ev -> {
						currentOrder.setStatus(OrderStatus.CANCELED);
						currentOrder.setTotal(0.0);
						currentOrder.setClosedAt(new Date());
						orderDao.save(currentOrder);
						subWindow.close();
						getUI().getNavigator().navigateTo(CloseOrderView.VIEW_NAME);
					});
					cancelLayout.addComponent(cancelBtn);

					subWindow.center();

					getUI().addWindow(subWindow);
				});

				Label currentType = new Label();
				btnLocal.addClickListener(event -> {
					orderType = OrderType.LOCAL;
					btnLocal.setEnabled(false);
					btnDeliver.setEnabled(false);
					btnTakeAway.setEnabled(false);

					btnDeliver.setVisible(false);
					btnTakeAway.setVisible(false);

					currentOrders.setItems(
							orderDao.findByStatusAndType(OrderStatus.OPENED, OrderType.LOCAL, currentRestaurant));
					currentOrders.setItemCaptionGenerator(
							o -> o.getZone().getDescription() + ", ID: " + o.getId() + ", " + o.getCreatedAt());
					currentOrders.setEmptySelectionAllowed(false);
					currentOrders.setWidth(400, Unit.PIXELS);
					hz.addComponent(new Label("Order: "));
					hz.addComponent(currentOrders);

					currentOrders.addSelectionListener(ev -> {
						options.removeAllComponents();
						currentOrder = orderDao.findOne(ev.getSelectedItem().get().getId());
						orderList.setItems(currentOrder.getLines());
						calculateLinesTotal();
						hzOrderListLayout.addComponent(orderList);
						options.addComponent(new Label("Status: " + currentOrder.getStatus()));
						options.addComponent(new Label("Created At: " + currentOrder.getCreatedAt()));
						options.addComponent(new Label("Zone: " + currentOrder.getZone().getDescription()));
						options.addComponent(new Label("Order type: " + currentOrder.getType()));
						options.addComponent(new Label("Waitter: " + currentUser.toString()));
						hzOrderListLayout.addComponent(options);
						orderListLayout.addComponent(hzOrderListLayout);
						orderListLayout.addComponent(closeOrderBtn);
						addComponent(orderListLayout);
					});

				});
				btnDeliver.addClickListener(event -> {
					orderType = OrderType.HOMEDELIVERY;
					btnLocal.setEnabled(false);
					btnDeliver.setEnabled(false);
					btnTakeAway.setEnabled(false);

					btnLocal.setVisible(false);
					btnTakeAway.setVisible(false);

					currentOrders.setItems(orderDao.findByStatusAndType(OrderStatus.OPENED, OrderType.HOMEDELIVERY,
							currentRestaurant));
					currentOrders.setItemCaptionGenerator(
							o -> o.getClient().getName() + ", " + o.getClient().getAddress() + ", "
									+ o.getClient().getPhoneNumber() + ", ID: " + o.getId() + ", " + o.getCreatedAt());
					currentOrders.setEmptySelectionAllowed(false);
					currentOrders.setWidth(400, Unit.PIXELS);
					hz.addComponent(new Label("Order: "));
					hz.addComponent(currentOrders);

					currentOrders.addSelectionListener(ev -> {
						options.removeAllComponents();
						currentOrder = orderDao.findOne(ev.getSelectedItem().get().getId());
						orderList.setItems(currentOrder.getLines());
						calculateLinesTotal();
						hzOrderListLayout.addComponent(orderList);
						options.addComponent(new Label("Status: " + currentOrder.getStatus()));
						options.addComponent(new Label("Created At: " + currentOrder.getCreatedAt()));
						options.addComponent(new Label("Client: " + currentOrder.getClient().getName() + ", "
								+ currentOrder.getClient().getAddress() + ", "
								+ currentOrder.getClient().getPhoneNumber()));
						options.addComponent(new Label("Order type: " + currentOrder.getType()));
						options.addComponent(new Label("Waitter: " + currentUser.toString()));
						hzOrderListLayout.addComponent(options);
						orderListLayout.addComponent(hzOrderListLayout);
						orderListLayout.addComponent(closeOrderBtn);
						addComponent(orderListLayout);
					});
				});
				btnTakeAway.addClickListener(event -> {
					orderType = OrderType.TOTAKEAWAY;
					btnLocal.setEnabled(false);
					btnDeliver.setEnabled(false);
					btnTakeAway.setEnabled(false);

					btnLocal.setVisible(false);
					btnDeliver.setVisible(false);

					currentOrders.setItems(orderDao.findByStatusAndType(OrderStatus.OPENED, OrderType.TOTAKEAWAY,
							currentRestaurant));
					currentOrders.setItemCaptionGenerator(
							o -> o.getClient().getName() + ", " + o.getClient().getAddress() + ", "
									+ o.getClient().getPhoneNumber() + ", ID: " + o.getId() + ", " + o.getCreatedAt());
					currentOrders.setEmptySelectionAllowed(false);
					currentOrders.setWidth(400, Unit.PIXELS);
					hz.addComponent(new Label("Order: "));
					hz.addComponent(currentOrders);

					currentOrders.addSelectionListener(ev -> {
						options.removeAllComponents();
						currentOrder = orderDao.findOne(ev.getSelectedItem().get().getId());
						orderList.setItems(currentOrder.getLines());
						calculateLinesTotal();
						hzOrderListLayout.addComponent(orderList);
						options.addComponent(new Label("Status: " + currentOrder.getStatus()));
						options.addComponent(new Label("Created At: " + currentOrder.getCreatedAt()));
						options.addComponent(new Label("Client: " + currentOrder.getClient().getName() + ", "
								+ currentOrder.getClient().getAddress() + ", "
								+ currentOrder.getClient().getPhoneNumber()));
						options.addComponent(new Label("Order type: " + currentOrder.getType()));
						options.addComponent(new Label("Waitter: " + currentUser.toString()));
						hzOrderListLayout.addComponent(options);
						orderListLayout.addComponent(hzOrderListLayout);
						orderListLayout.addComponent(closeOrderBtn);
						addComponent(orderListLayout);
					});
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
