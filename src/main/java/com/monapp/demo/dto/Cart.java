package com.monapp.demo.dto;

import com.monapp.demo.Entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@NoArgsConstructor
public class Cart {
    private Map<Long, CartItem> items = new HashMap<>();
    
    public void addItem(Product product, int quantity) {
        if (items.containsKey(product.getId())) {
            CartItem existingItem = items.get(product.getId());
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.put(product.getId(), new CartItem(product, quantity));
        }
    }
    
    public void removeItem(Long productId) {
        items.remove(productId);
    }
    
    public void updateQuantity(Long productId, int quantity) {
        if (items.containsKey(productId)) {
            if (quantity <= 0) {
                items.remove(productId);
            } else {
                items.get(productId).setQuantity(quantity);
            }
        }
    }
    
    public void clear() {
        items.clear();
    }
    
    public List<CartItem> getCartItems() {
        return new ArrayList<>(items.values());
    }
    
    public double getTotal() {
        return items.values().stream()
            .mapToDouble(CartItem::getTotalPrice)
            .sum();
    }
    
    public int getItemCount() {
        return items.values().stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
}