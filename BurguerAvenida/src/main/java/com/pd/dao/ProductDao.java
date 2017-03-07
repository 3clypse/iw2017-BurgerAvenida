package com.pd.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.Product;

@Repository
public interface ProductDao extends CrudRepository<Product, Integer> {

}
