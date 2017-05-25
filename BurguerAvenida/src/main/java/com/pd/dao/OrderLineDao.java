package com.pd.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.OrderLine;
import com.pd.model.OrderLinePK;

@Repository
public interface OrderLineDao extends CrudRepository<OrderLine, OrderLinePK>{

}
