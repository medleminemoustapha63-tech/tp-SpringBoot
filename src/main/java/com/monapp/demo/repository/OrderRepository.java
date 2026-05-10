package com.monapp.demo.repository;

import com.monapp.demo.Entity.Order;
import com.monapp.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Récupérer toutes les commandes d'un utilisateur triées par date décroissante
    List<Order> findByUserOrderByOrderDateDesc(User user);
    
    // Récupérer les commandes par statut
    List<Order> findByStatus(String status);
    
    // Récupérer les commandes d'un utilisateur par statut
    List<Order> findByUserAndStatus(User user, String status);
}