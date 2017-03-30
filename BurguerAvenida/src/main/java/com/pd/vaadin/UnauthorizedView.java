package com.pd.vaadin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = UnauthorizedView.VIEW_NAME)
public class UnauthorizedView extends VerticalLayout implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6711014872706178790L;
	
	public static final String VIEW_NAME = "Error";
	
	private Label header;
	private HorizontalLayout headerRow;
	
    public UnauthorizedView() {
    	setSizeFull();
    	headerRow = new HorizontalLayout();
    	headerRow.setWidth("100%");
    	addComponent(headerRow);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    	header = new Label("Error 401 : UNAUTHORIZED");
    	header.addStyleName(ValoTheme.LABEL_FAILURE);
    	headerRow.addComponent(header);
    	headerRow.setComponentAlignment(header, Alignment.MIDDLE_CENTER);
    }
}
