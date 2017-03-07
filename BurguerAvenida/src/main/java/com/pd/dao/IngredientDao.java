package com.pd.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.Ingredient;

@Repository
public interface IngredientDao extends CrudRepository<Ingredient, Integer>{

}
