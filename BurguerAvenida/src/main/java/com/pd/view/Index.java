package com.pd.view;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@Theme("valo")
@SpringUI(path = "")
public class Index extends UI {

	 /**
	 * 
	 */
	private static final long serialVersionUID = -6457669656179486858L;

	@Override
     protected void init(VaadinRequest request) {
         setContent(new Label("Index")); 
     }
	
}
