package com.pd.vaadin.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.VerticalLayout;

@ViewScope
@SpringView(name = CloseOrderView.VIEW_ROUTE)
public class CloseOrderView extends VerticalLayout implements View  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8521467936052410664L;
	
	public static final String VIEW_ROUTE = "CloseOrderView";
	public static final String VIEW_NAME = "CloseOrderView";
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
