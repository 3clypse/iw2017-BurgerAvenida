package com.pd.vaadin.about;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = AboutView.VIEW_NAME)
public class AboutView extends VerticalLayout implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = -678558853372417010L;
	public static final String VIEW_NAME = "About";

    public AboutView() {
        setSizeFull();
        Label header = new Label("About");
        header.addStyleName(ValoTheme.LABEL_H2);
        addComponent(header);
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }

}
