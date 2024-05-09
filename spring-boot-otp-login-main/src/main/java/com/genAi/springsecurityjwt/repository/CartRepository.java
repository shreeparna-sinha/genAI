package com.genAi.springsecurityjwt.repository;

import org.springframework.data.repository.CrudRepository;

import com.genAi.springsecurityjwt.model.CartItem;

public interface CartRepository  extends CrudRepository<CartItem, Long>{

}
