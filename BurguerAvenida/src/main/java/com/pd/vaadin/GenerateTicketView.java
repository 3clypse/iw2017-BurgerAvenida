package com.pd.vaadin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.pd.dao.ClientDao;
import com.pd.dao.OrderDao;
import com.pd.dao.OrderLineDao;
import com.pd.dao.ProductDao;
import com.pd.dao.RestaurantDao;
import com.pd.dao.ZoneDao;
import com.pd.dao.security.UserDao;
import com.pd.model.Order;
import com.pd.model.OrderStatus;
import com.pd.model.OrderType;
import com.pd.model.Restaurant;
import com.pd.model.security.User;
import com.pd.vaadin.utils.SecurityUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Scope("prototype")
@SpringView(name = GenerateTicketView.VIEW_ROUTE)
public class GenerateTicketView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7560652561089062566L;

	public static final String VIEW_ROUTE = "GenerateTicketView";
	public static final String VIEW_NAME = "GenerateTicketView";
	 public static final String IMAGE = "src/main/resources/VAADIN/themes/mytheme/img/iwLogo.png";

	private final OrderDao orderDao;

	private final ProductDao productDao;

	private final RestaurantDao restaurantDao;

	private final UserDao userDao;

	private final OrderLineDao orderLineDao;

	private final ZoneDao zoneDao;

	private final ClientDao clientDao;

	private Order currentOrder;
	private User currentUser;
	private Label labelRestaurant;
	private Restaurant currentRestaurant;
	private VerticalLayout verticalTitleLayout;
	private HorizontalLayout hz;
	private OrderType orderType;
	private Grid<Order> orderList = new Grid<>("Closed Orders");
	private HorizontalLayout pdfOptions = new HorizontalLayout();

	@Autowired
	public GenerateTicketView(OrderDao orderDao, ProductDao productDao, RestaurantDao restaurantDao, UserDao userDao,
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
		Label header = new Label("Generate Ticker View");
		header.addStyleName(ValoTheme.LABEL_H2);
		header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		verticalTitleLayout = new VerticalLayout(new HorizontalLayout(header));
		verticalTitleLayout.setMargin(new MarginInfo(false, false, false, true));
		addComponent(verticalTitleLayout);
		loadRestaurant();

	}
	
	private void createPdfTicket(Order currentOrder) throws FileNotFoundException, DocumentException {
		final TextField name = new TextField();

		final Button ok = new Button("Set name");
		

		ok.addClickListener(event -> {
			
			Document pdf = new Document(PageSize.A6);
			pdf.setMargins(10,10,10,10);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PdfWriter writer = null;
			try {
				writer = PdfWriter.getInstance(pdf, out);
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
			pdf.open();
			try {
				PdfContentByte canvas = writer.getDirectContentUnder();
		        Image image = Image.getInstance(IMAGE);
		        image.scaleAbsolute(PageSize.A4.rotate());
		        image.setAbsolutePosition(10f, 350f);
		        float scaler = ((pdf.getPageSize().getWidth() - pdf.leftMargin()
		                - pdf.rightMargin() - 10) / image.getWidth()) * 100;

		        image.scalePercent(scaler);
		        canvas.addImage(image);
		        
		        pdf.add(new Paragraph("\n"));
				pdf.add(new Paragraph("\n"));
				pdf.add(new Paragraph("\n"));
				pdf.add(new Paragraph("\n"));
				Paragraph title1 = new Paragraph(currentOrder.getRestaurant().getName()+", "+currentOrder.getRestaurant().getAddress());
				title1.setAlignment(Element.ALIGN_CENTER);
				pdf.add(title1);
				Paragraph title2 = new Paragraph();
				if(currentOrder.getType() == OrderType.LOCAL) {
					title2 = new Paragraph(currentOrder.getType().toString()+", "+currentOrder.getZone().getDescription()+", IVA Incluido");
				}else{
					title2 = new Paragraph(currentOrder.getType().toString()+", "+currentOrder.getClient().getName()+", IVA Incluido");
				}
				title2.setAlignment(Element.ALIGN_CENTER);
				pdf.add(title2);
				LineSeparator ls = new LineSeparator();
				pdf.add(new Chunk(ls));
				pdf.add(new Paragraph("\n"));
				PdfPTable table = new PdfPTable(4);
				table.addCell("#");
				table.addCell("Description");
				table.addCell("U.P.");
				table.addCell("Total");
				//table.setWidthPercentage(288 / 4.23f);
				table.setWidths(new int[]{1, 3, 1, 1});
		        currentOrder.getLines().forEach(l->{
		        	table.addCell(l.getAmount().toString());
		        	table.addCell(l.getProduct().getName());
		        	table.addCell(l.getProduct().getPrice().toString());
		        	table.addCell(BigDecimal.valueOf(l.getTotal()).setScale(2, RoundingMode.HALF_UP).toString());
		        });
		        
		        pdf.add(table);
		        
		        Paragraph total = new Paragraph("Total: " +currentOrder.getTotal());
		        total.setAlignment(Element.ALIGN_CENTER);
				pdf.add(total);
				
				pdf.add(new Chunk(ls));
				pdf.add(new Chunk(ls));
				
				Paragraph last = new Paragraph("See you soon! - " +currentOrder.getClosedAt());
				last.setAlignment(Element.ALIGN_CENTER);
				pdf.add(last);
		        
		        
			} catch (DocumentException | IOException e) {
				e.printStackTrace();
			}
			pdf.close();
			
			Button btn = new Button("Download");
			pdfOptions.addComponent(btn);
			FileDownloader fd = new FileDownloader(
					new StreamResource(() -> 
					new ByteArrayInputStream(out.toByteArray()), name.getValue()+".pdf"));
			fd.extend(btn);
		});

		pdfOptions.addComponent(name);
		pdfOptions.addComponent(ok);
		addComponent(pdfOptions);

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

				orderList.addColumn(Order::getId).setCaption("Id").setId("id");
				orderList.addColumn(Order::getCreatedAt).setCaption("Created at").setId("createdat");
				orderList.addColumn(Order::getClosedAt).setCaption("Closed at").setId("closedat");
				orderList.setSizeFull();

				orderList.addItemClickListener(e -> {
					try {
						pdfOptions.removeAllComponents();
						createPdfTicket(e.getItem());
					} catch (FileNotFoundException | DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});

				Label currentType = new Label();
				btnLocal.addClickListener(event -> {
					orderType = OrderType.LOCAL;
					btnLocal.setEnabled(false);
					btnDeliver.setEnabled(false);
					btnTakeAway.setEnabled(false);

					btnDeliver.setVisible(false);
					btnTakeAway.setVisible(false);

					orderList.addColumn(Order::getZone).setCaption("Zone").setId("zone");
					orderList.addColumn(Order::getTotal).setCaption("Total").setId("total");
					orderList.setItems(
							orderDao.findByStatusAndType(OrderStatus.CLOSED, OrderType.LOCAL, currentRestaurant));

					addComponent(orderList);

				});
				btnDeliver.addClickListener(event -> {
					orderType = OrderType.HOMEDELIVERY;
					btnLocal.setEnabled(false);
					btnDeliver.setEnabled(false);
					btnTakeAway.setEnabled(false);

					btnLocal.setVisible(false);
					btnTakeAway.setVisible(false);

					orderList.addColumn(Order::getClient).setCaption("Client").setId("client");
					orderList.addColumn(Order::getTotal).setCaption("Total").setId("total");
					orderList.setItems(orderDao.findByStatusAndType(OrderStatus.CLOSED, OrderType.HOMEDELIVERY,
							currentRestaurant));

					addComponent(orderList);

				});
				btnTakeAway.addClickListener(event -> {
					orderType = OrderType.TOTAKEAWAY;
					btnLocal.setEnabled(false);
					btnDeliver.setEnabled(false);
					btnTakeAway.setEnabled(false);

					btnLocal.setVisible(false);
					btnDeliver.setVisible(false);

					orderList.addColumn(Order::getClient).setCaption("Client").setId("client");
					orderList.addColumn(Order::getTotal).setCaption("Total").setId("total");
					orderList.setItems(
							orderDao.findByStatusAndType(OrderStatus.CLOSED, OrderType.TOTAKEAWAY, currentRestaurant));

					addComponent(orderList);
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
