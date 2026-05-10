package com.monapp.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private String actions;
    private int stock;          // ← ajouté pour correspondre au formulaire

    // Constructeur utile sans l'id (auto-généré)
    public Product(String name, String description, double price, String actions, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.actions = actions;
        this.stock = stock;
    }
}