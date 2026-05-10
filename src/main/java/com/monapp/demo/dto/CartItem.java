package com.monapp.demo.dto;

import com.monapp.demo.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;
    
    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}

