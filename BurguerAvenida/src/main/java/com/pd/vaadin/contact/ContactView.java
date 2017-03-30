package com.pd.vaadin.contact;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = ContactView.VIEW_NAME)
public class ContactView extends VerticalLayout implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6053541745106875920L;
	public static final String VIEW_NAME = "Contact";

    public ContactView() {
    	 setSizeFull();
         Label header = new Label("Contact");
         header.addStyleName(ValoTheme.LABEL_H2);
         addComponent(header);
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }

}
