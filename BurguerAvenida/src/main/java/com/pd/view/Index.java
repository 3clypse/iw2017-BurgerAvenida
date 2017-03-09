package com.pd.view;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("valo")
@SpringUI(path = "/")
public class Index extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Index.class, widgetset = "com.example.vaadinmenu.widgetset.VaadinmenuWidgetset")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);

		HorizontalLayout menuLayout = new HorizontalLayout();
		menuLayout.setSpacing(true);
		
		Label myFirstLabel = new Label("-");
		myFirstLabel.addStyleName("myfirstlabel");
		
		Label mySecondLabel = new Label("-");
		mySecondLabel.addStyleName("mysecondlabel");
		
		Label secondMenuLabel = new Label("-");
		secondMenuLabel.addStyleName("secondmenulabel");
		
		MenuBar.Command myFirstCommand = new MenuBar.Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				myFirstLabel.setValue(selectedItem.getText());
				if(selectedItem.hasChildren()){
					List<MenuItem> items = selectedItem.getChildren();
					StringBuilder sb = new StringBuilder();
					for(MenuItem item : items){
						sb.append(item.getText());
					}
					mySecondLabel.setValue(sb.toString());
				}else{
					mySecondLabel.setValue("-");
				}
				secondMenuLabel.setValue("-");
			}
		};
		
		MenuBar.Command mySecondCommand = new MenuBar.Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				mySecondLabel.setValue(selectedItem.getText());
				MenuItem parent = selectedItem.getParent();
				if(parent!=null){
					myFirstLabel.setValue(parent.getText());
				}
				secondMenuLabel.setValue("-");
			}
		};

		MenuBar.Command secondMenuCommand = new MenuBar.Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				myFirstLabel.setValue("-");
				mySecondLabel.setValue("-");
				secondMenuLabel.setValue("second menu clicked " + selectedItem.getText());
			}
		};

		MenuBar menu = new MenuBar();
		menu.addStyleName("firstmenu");
		MenuItem edit = menu.addItem("Edit", null);
		MenuItem help = menu.addItem("Help", null);
		
		help.addItem("Search", myFirstCommand);
		MenuItem index = help.addItem("Index", myFirstCommand);
		MenuItem level01 = index.addItem("level01", mySecondCommand);
		MenuItem level02 = level01.addItem("level02", mySecondCommand);
		level02.addItem("level03", mySecondCommand);
		help.addSeparator();
		help.addItem("About", myFirstCommand);

		
		
		
		
		
		
		
		
		//Menu2	
		MenuBar barmenu = new MenuBar();
		barmenu.addStyleName("mybarmenu");
		layout.addComponent(barmenu);
		layout.setComponentAlignment(barmenu, Alignment.TOP_CENTER);

		// A feedback component
		final Label selection = new Label("-");
		layout.addComponent(selection);

		// Define a common menu command for all the menu items
		MenuBar.Command mycommand = new MenuBar.Command() {
		    MenuItem previous = null;

		    public void menuSelected(MenuItem selectedItem) {
		        selection.setValue("Ordered a " +
		                selectedItem.getText() +
		                " from menu.");

		        if (previous != null)
		            previous.setStyleName(null);
		        selectedItem.setStyleName("highlight");
		        previous = selectedItem;
		    }
		};

		MenuItem drinks = barmenu.addItem("Beverages", null, null);
		// Another submenu item with a sub-submenu
		MenuItem colds = drinks.addItem("Cold", null, null);
		colds.addItem("Milk",      null, mycommand);
		colds.addItem("Weissbier", null, mycommand);

		// Another top-level item
		MenuItem snacks = barmenu.addItem("Snacks", null, null);
		snacks.addItem("Weisswurst", null, mycommand);
		snacks.addItem("Bratwurst",  null, mycommand);
		snacks.addItem("Currywurst", null, mycommand);

		// Yet another top-level item
		MenuItem servs = barmenu.addItem("Services", null, null);
		servs.addItem("Car Service", null, mycommand);
	}
	

}