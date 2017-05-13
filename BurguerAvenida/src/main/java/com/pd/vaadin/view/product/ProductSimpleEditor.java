package com.pd.vaadin.view.product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.ProductFamilyDao;
import com.pd.dao.ProductSimpleDao;
import com.pd.model.IVA;
import com.pd.model.ProductFamily;
import com.pd.model.ProductSimple;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringComponent
public class ProductSimpleEditor extends FormLayout implements Receiver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1700349086596241763L;

	
	private final ProductSimpleDao repository;
	
	@SuppressWarnings("unused")
	private final ProductFamilyDao familyDao;
	
	/**
	 * The currently object
	 */
	private ProductSimple currentObject;

	TextField name = new TextField("Name");
	TextField price = new TextField("Price");
	ComboBox<IVA> iva = new ComboBox<IVA>("IVA");
	ComboBox<Boolean> canBeSoldAlone = new ComboBox<Boolean>("Can be sold alone");
	TwinColSelect<ProductFamily> families = new TwinColSelect<ProductFamily>("Product families");
	
	final Upload upload = new Upload("Imagen upload", this);
	HorizontalLayout imageLayout = new HorizontalLayout();
	
	Button save = new Button("Save", VaadinIcons.SAFE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcons.ERASER);
	CssLayout actions = new CssLayout(save, cancel, delete);

	File file;
	Binder<ProductSimple> binder;

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		  FileOutputStream fos = null;
	      file = new File(filename);
	      try {
	          fos = new FileOutputStream(file);
	      } catch (final java.io.FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	      }
	      return fos;
	}
	
	@Autowired
	public ProductSimpleEditor(ProductSimpleDao repository, ProductFamilyDao familyDao) {
		this.repository = repository;
		this.familyDao = familyDao;

		iva.setEmptySelectionAllowed(false);
		upload.setImmediateMode(false);
		upload.setButtonCaption("Upload Now");
		
		iva.setItems(EnumSet.allOf(IVA.class));
		families.setItems((Collection<ProductFamily>) familyDao.findAll());
		canBeSoldAlone.setEmptySelectionAllowed(false);
		canBeSoldAlone.setItems(Arrays.asList(true, false));
		addComponents(name, price, iva, canBeSoldAlone, families, upload, actions);
		
		binder = new BeanValidationBinder<>(ProductSimple.class);
		iva.setItemCaptionGenerator(iva -> iva+", "+iva.getIVA());
		binder.bindInstanceFields(this);

		name.setSizeFull();
		price.setSizeFull();
		canBeSoldAlone.setSizeFull();
		iva.setSizeFull();
		families.setSizeFull();
		
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		save.addClickListener(e -> {
			if(file != null) currentObject.setImage(file);
			repository.save(currentObject);
		});
		delete.addClickListener(e -> repository.delete(currentObject));
		cancel.addClickListener(e -> setVisible(false));
		setVisible(false);
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void edit(ProductSimple c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			currentObject = repository.findOne(c.getId());
		}
		else {
			currentObject = c;
		}
		
		cancel.setVisible(persisted);
		
		binder.setBean(currentObject);
		
		setVisible(true);
	}
	
	public void setChangeHandler(ChangeHandler h) {
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}

}