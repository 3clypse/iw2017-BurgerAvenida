package com.pd.vaadin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = ErrorView.VIEW_NAME)
public class ErrorView extends VerticalLayout implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6711014872706178790L;
	
	public static final String VIEW_NAME = "Error";
	
	private String explanation;
	private Label header;
	private HorizontalLayout headerRow;
	
    public ErrorView() {
    	setSizeFull();
    	headerRow = new HorizontalLayout();
    	headerRow.setWidth("100%");
    	addComponent(headerRow);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    	explanation = event.getViewName();
    	header = new Label("Error 404 : The view [ "+ explanation +" ] could not be found");
    	header.addStyleName(ValoTheme.LABEL_FAILURE);
    	headerRow.addComponent(header);
    	headerRow.setComponentAlignment(header, Alignment.MIDDLE_CENTER);
    }
}
