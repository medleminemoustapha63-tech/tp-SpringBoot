package com.monapp.demo.repository;

import com.monapp.demo.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    // Récupérer tous les items d'une commande
    List<OrderItem> findByOrderId(Long orderId);
}