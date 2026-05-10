package com.monapp.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    
    @Column(name = "total_amount", nullable = false)
    private double totalAmount;
    
    @Column(nullable = false, length = 20)
    private String status;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    // Constructeur pratique
    public Order(User user) {
        this.user = user;
        this.orderDate = LocalDateTime.now();
        this.status = "CONFIRMED";
        this.totalAmount = 0;
        this.orderItems = new ArrayList<>();
    }
    
    // Méthode pour ajouter un produit à la commande
    public void addProduct(Product product, int quantity) {
        OrderItem orderItem = new OrderItem(this, product, quantity, product.getPrice());
        this.orderItems.add(orderItem);
        this.totalAmount += product.getPrice() * quantity;
    }
}