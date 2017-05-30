package com.pd.vaadin.view.product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.pd.dao.ProductCompositeDao;
import com.pd.dao.ProductDao;
import com.pd.dao.ProductFamilyDao;
import com.pd.model.IVA;
import com.pd.model.Product;
import com.pd.model.ProductComposite;
import com.pd.model.ProductFamily;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringComponent
public class ProductCompositeEditor extends FormLayout implements Receiver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1700349086596241763L;

	private final ProductCompositeDao repository;

	@SuppressWarnings("unused")
	private final ProductFamilyDao familyDao;

	@SuppressWarnings("unused")
	private final ProductDao productDao;

	/**
	 * The currently object
	 */
	private ProductComposite currentObject;

	TextField name = new TextField("Name");
	TextField price = new TextField("Price");
	ComboBox<IVA> iva = new ComboBox<IVA>("IVA");
	ComboBox<Boolean> canBeSoldAlone = new ComboBox<Boolean>("Can be sold alone");
	TwinColSelect<Product> products = new TwinColSelect<Product>("Products");
	TwinColSelect<ProductFamily> families = new TwinColSelect<ProductFamily>("Product families");
	
	final Upload upload = new Upload("Imagen upload", this);
	HorizontalLayout imageLayout = new HorizontalLayout();

	Button save = new Button("Save", VaadinIcons.SAFE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcons.ERASER);
	CssLayout actions = new CssLayout(save, cancel, delete);

	File file;
	Binder<ProductComposite> binder;

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fos = null;
		file = new File(filename);
		try {
			System.out.println(filename);
			fos = new FileOutputStream(file);
		} catch (final java.io.FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return fos;
	}

	@Autowired
	public ProductCompositeEditor(ProductCompositeDao repository, ProductFamilyDao familyDao, ProductDao productDao) {
		this.repository = repository;
		this.familyDao = familyDao;
		this.productDao = productDao;

		iva.setEmptySelectionAllowed(false);

		iva.setItems(EnumSet.allOf(IVA.class));
		products.setItems((Collection<Product>) productDao.findAll());
		families.setItems((Collection<ProductFamily>) familyDao.findAll());

		upload.setImmediateMode(false);
		upload.setButtonCaption("Upload Now");
		canBeSoldAlone.setEmptySelectionAllowed(false);
		canBeSoldAlone.setItems(Arrays.asList(true, false));

		addComponents(name, price, iva, canBeSoldAlone, upload, products, families, actions);

		binder = new BeanValidationBinder<>(ProductComposite.class);
		iva.setItemCaptionGenerator(iva -> iva + ", " + iva.getIVA());

		name.setSizeFull();
		price.setSizeFull();
		iva.setSizeFull();
		canBeSoldAlone.setSizeFull();
		products.setSizeFull();
		families.setSizeFull();

		name.setMaxLength(32);
		price.setMaxLength(16);

		binder.forField(name).asRequired("Cant be empty")
				.withValidator(new StringLengthValidator("Address must be between 2 and 32 characters long", 2, 32))
				.bind(Product::getName, Product::setName);

		binder.forField(price)
		.asRequired("Cant be empty")
		.withNullRepresentation("")
		.withConverter(new StringToDoubleConverter("Must enter a number"))
		.bind(ProductComposite::getPrice, ProductComposite::setPrice);
		
		binder.forField(iva).bind(ProductComposite::getIva, ProductComposite::setIva);
		
		binder.forField(canBeSoldAlone).bind(ProductComposite::getCanBeSoldAlone, ProductComposite::setCanBeSoldAlone);

		binder.forField(products).bind(ProductComposite::getProducts, ProductComposite::setProducts);
		
		binder.forField(families).bind(ProductComposite::getFamilies, ProductComposite::setFamilies);

		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		save.addClickListener(e -> {
			if (binder.isValid()) {
				if (file != null)
					currentObject.setImage(file);
				repository.save(currentObject);
			} else
				showNotification(new Notification("Some fields are not valid"));
		});
		delete.addClickListener(e -> repository.delete(currentObject));
		cancel.addClickListener(e -> setVisible(false));
		setVisible(false);
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void edit(ProductComposite c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			currentObject = repository.findOne(c.getId());
		} else {
			currentObject = c;
		}

		cancel.setVisible(persisted);

		if (currentObject != null)
			binder.setBean(currentObject);
		else {
			binder.writeBeanIfValid(currentObject);
		}

		setVisible(true);

		save.focus();
		name.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		save.addClickListener(e -> {
			if (binder.isValid())
				h.onChange();
		});
		delete.addClickListener(e -> h.onChange());
	}

	private void showNotification(Notification notification) {
		notification.setDelayMsec(2000);
		notification.show(Page.getCurrent());
	}

}