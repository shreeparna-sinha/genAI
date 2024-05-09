package com.genAi.springsecurityjwt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genAi.springsecurityjwt.model.CartItem;
import com.genAi.springsecurityjwt.model.Product;
import com.genAi.springsecurityjwt.model.User;
import com.genAi.springsecurityjwt.repository.CartRepository;
import com.genAi.springsecurityjwt.repository.UserRepository;

@Service
public class CartService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private CartRepository cartRepository;

	public List<CartItem> addToCart(Long userId, Long productId, int quantity) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    Product product = productService.getProductById(productId)
	            .orElseThrow(() -> new RuntimeException("Product not found"));

	    // Check if the product is already in the cart
	    CartItem existingItem = findCartItemByProduct(user.getCart(), product);
	    CartItem newItem = new CartItem();

	    if (existingItem != null) {
	        // Update existing item quantity
	        existingItem.setQuantity(existingItem.getQuantity() + quantity);
	    } else {
	        // Create a new cart item
	        newItem.setProduct(product); // Set product as a list
	        newItem.setQuantity(quantity);

	        // Optional: Set user association (if necessary)
	        // newItem.setUser(user);

	        // Add to user's cart (assuming a one-to-many relationship)
	        user.getCart().add(newItem);
	    }

	    cartRepository.save(existingItem != null ? existingItem : newItem); // Save updated item
	    userRepository.save(user); // Save user, implicitly saving the cart

	    // Refresh user from the database to ensure consistent state
	    user = userRepository.findById(userId).orElseThrow();
	    return user.getCart();
	}

	private CartItem findCartItemByProduct(List<CartItem> cartItems, Product product) {
	    for (CartItem item : cartItems) {
	        if (item.getProduct().getId().equals(product.getId())) {
	            return item;
	        }
	    }
	    return null;
	}


    public List<CartItem> getCart(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getCart();
    }

//	private CartItem findCartItemByProduct(List<CartItem> cart, Product product) {
//		return cart.stream().filter(cartItem -> cartItem.getProduct().equals(product)).findFirst().orElse(null);
//	}
    public List<CartItem> removeFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productService.getProductById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        // Remove the item from the cart
        CartItem itemToRemove = findCartItemByProduct(user.getCart(), product);
        if (itemToRemove != null) {
            user.getCart().remove(itemToRemove);
            userRepository.save(user);
        }
        Optional<User> u = userRepository.findById(userId);
        return u.get().getCart();
        
    }
}
