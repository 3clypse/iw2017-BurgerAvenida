package com.pd.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import com.vaadin.server.StreamResource;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ProductType")
public class Product {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO) 
	private Integer id;
	
	@Column(length = 32, unique = true)
	private String name;
	
	private String price;
	
	private IVA iva;
	
	private Boolean canBeSoldAlone;
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private byte[] image;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<ProductFamily> families = new HashSet<ProductFamily>();
	
	public Product() {
		super();
	}
	
	public Product(String name, String price, IVA iva, Boolean canBeSoldAlone) {
		super();
		this.name = name;
		this.price = price;
		this.iva = iva;
		this.canBeSoldAlone = canBeSoldAlone;
	}
	
	public Product(String name, String price, IVA iva, Set<ProductFamily> families, Boolean canBeSoldAlone) {
		super();
		this.name = name;
		this.price = price;
		this.iva = iva;
		this.families = families;
		this.canBeSoldAlone = canBeSoldAlone;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public IVA getIva() {
		return iva;
	}

	public void setIva(IVA iva) {
		this.iva = iva;
	}

	public Set<ProductFamily> getFamilies() {
		return families;
	}

	public void setFamilies(Set<ProductFamily> families) {
		this.families = families;
	}
	
	public Boolean getCanBeSoldAlone() {
		return canBeSoldAlone;
	}

	public void setCanBeSoldAlone(Boolean canBeSoldAlone) {
		this.canBeSoldAlone = canBeSoldAlone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public StreamResource getImage() {
		return new StreamResource(() -> new ByteArrayInputStream(image), UUID.randomUUID().toString());
	}

	public void setImage(File file) {
		byte[] picInBytes = new byte[(int) file.length()];
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(picInBytes);
			fileInputStream.close();
			file.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.image = picInBytes;
	}
	
}
