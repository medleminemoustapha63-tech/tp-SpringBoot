package com.monapp.demo.service;

import com.monapp.demo.Entity.Order;
import com.monapp.demo.Entity.OrderItem;
import com.monapp.demo.Entity.Product;
import com.monapp.demo.Entity.User;
import com.monapp.demo.repository.OrderItemRepository;
import com.monapp.demo.repository.OrderRepository;
import com.monapp.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    
    // Récupérer toutes les commandes d'un utilisateur
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
    
    // Récupérer une commande par son ID
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    // Annuler une commande
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
        
        // Vérifier si la commande peut être annulée
        if (order.getStatus().equals("CONFIRMED") || order.getStatus().equals("PENDING")) {
            order.setStatus("CANCELLED");
            orderRepository.save(order);
            log.info("Commande #{} annulée avec succès", orderId);
        } else {
            throw new RuntimeException("Impossible d'annuler une commande avec le statut: " + order.getStatus());
        }
    }
    
    // Créer une nouvelle commande
    @Transactional
    public Order createOrder(List<Product> products, List<Integer> quantities, User user) {
        if (products.size() != quantities.size()) {
            throw new RuntimeException("Le nombre de produits et quantités ne correspond pas");
        }
        
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CONFIRMED");
        
        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            Integer quantity = quantities.get(i);
            
            if (product == null) {
                throw new RuntimeException("Produit non trouvé");
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice());
            
            double itemTotal = product.getPrice() * quantity;
            totalAmount += itemTotal;
            
            orderItems.add(orderItem);
        }
        
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
        
        Order savedOrder = orderRepository.save(order);
        
        // Sauvegarder les items
        for (OrderItem item : orderItems) {
            orderItemRepository.save(item);
        }
        
        log.info("Commande #{} créée pour {} avec {} articles", 
            savedOrder.getId(), user.getEmail(), orderItems.size());
        
        return savedOrder;
    }
    
    // Mettre à jour le statut d'une commande
    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
        
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
    // Récupérer les commandes par statut
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
    
    // Récupérer les commandes d'un utilisateur par statut
    public List<Order> getOrdersByUserAndStatus(User user, String status) {
        return orderRepository.findByUserAndStatus(user, status);
    }
}