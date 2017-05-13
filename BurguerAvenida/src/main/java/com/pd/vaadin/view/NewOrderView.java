package com.pd.vaadin.view;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.OrderDao;
import com.pd.dao.ProductDao;
import com.pd.model.Order;
import com.pd.model.OrderLine;
import com.pd.model.Product;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
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

	public static final String VIEW_ROUTE = "NewOrderView";
	public static final String VIEW_NAME = "NewOrderView";

	GridLayout options;
	Grid<OrderLine> orderList;
	Order order;
	Set<OrderLine> orderLineSet = new HashSet<OrderLine>();
	OrderLine orderline;
	Button makeOrder = new Button("Open new order");

	private boolean alreadyExists;
	private FooterCell footer;

	@Autowired
	public NewOrderView(OrderDao orderDao, ProductDao productDao) {
		this.orderDao = orderDao;
		this.productDao = productDao;
	}

	@PostConstruct
	void init() {
		this.setResponsive(true);
		Label header = new Label("New Order");
		header.addStyleName(ValoTheme.LABEL_H2);
		header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		VerticalLayout verticalTitleLayout = new VerticalLayout(new HorizontalLayout(header));
		verticalTitleLayout.setMargin(new MarginInfo(false, false, false, true));
		addComponent(verticalTitleLayout);

		VerticalLayout orderListLayout = new VerticalLayout();
		orderList = new Grid<OrderLine>();
		orderList.addColumn(OrderLine::getProduct).setCaption("Product name").setId("productname");
		orderList.addColumn(OrderLine::getAmount).setCaption("Amount").setId("amount");
		orderList.addColumn(OrderLine::getTotal).setCaption("Total").setId("total");
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

		options = new GridLayout(10,10);
		options.setResponsive(true);
		options.setMargin(new MarginInfo(false, false, false, false));
		options.setWidth(100, Unit.PERCENTAGE);

		makeOrder.addClickListener(event -> {
			if(orderLineSet.isEmpty())
				showNotification(new Notification("Order cant be empty!"));
			else {
				order.setLines(orderLineSet);
				orderDao.save(order);
				order = new Order();
				orderLineSet.clear();
				orderList.setItems(orderLineSet);
				calculateLinesTotal();
				showNotification(new Notification("Order was processed"));
			}
		});
		orderListLayout.addComponent(makeOrder);
		orderListLayout.addComponent(options);
		addComponent(orderListLayout);
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

	@Override
	public void enter(ViewChangeEvent event) {
		options.removeAllComponents();
		loadImage();
	}

}
