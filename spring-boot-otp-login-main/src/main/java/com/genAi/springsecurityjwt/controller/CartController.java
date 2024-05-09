package com.genAi.springsecurityjwt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genAi.springsecurityjwt.model.CartItem;
import com.genAi.springsecurityjwt.service.CartService;

@RestController
@RequestMapping(value ="/api/cart")
@CrossOrigin("*")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public List<CartItem> addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        System.out.println(userId);
        System.out.println(productId);
        System.out.println(quantity);
    	return cartService.addToCart(userId, productId, quantity);
//        return ResponseEntity.ok().build();
    }
//
    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Long userId) {
    			return cartService.getCart(userId);
    }
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok().build();
    }
}
