package com.monapp.demo.controller;

import com.monapp.demo.Entity.Order;
// import com.monapp.demo.Entity.OrderItem;
// import com.monapp.demo.Entity.Product;
import com.monapp.demo.Entity.User;
import com.monapp.demo.repository.UserRepository;
import com.monapp.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/mes-commandes")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    private final UserRepository userRepository;
    
    
    
    // Afficher toutes les commandes
    @GetMapping
    public String showOrders(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth == null || !auth.isAuthenticated()) {
                log.warn("Utilisateur non authentifié");
                return "redirect:/login";
            }
            
            User user = getCurrentUser(auth);
            
            if (user == null) {
                log.error("Impossible de trouver l'utilisateur");
                model.addAttribute("error", "Erreur de session utilisateur");
                return "redirect:/login";
            }
            
            List<Order> orders = orderService.getOrdersByUser(user);
            model.addAttribute("orders", orders);
            
            log.info("Affichage de {} commandes pour {}", orders.size(), user.getEmail());
            
            return "shop/mes-commandes";
            
        } catch (Exception e) {
            log.error("Erreur détaillée: ", e);
            model.addAttribute("error", "Erreur: " + e.getMessage());
            return "shop/mes-commandes";
        }
    }
    
    // Afficher les détails d'une commande spécifique
    @GetMapping("/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth == null || !auth.isAuthenticated()) {
                log.warn("Utilisateur non authentifié");
                return "redirect:/login";
            }
            
            User currentUser = getCurrentUser(auth);
            
            if (currentUser == null) {
                log.error("Impossible de trouver l'utilisateur");
                redirectAttributes.addFlashAttribute("error", "Erreur de session utilisateur");
                return "redirect:/login";
            }
            
            // Récupérer la commande
            Optional<Order> orderOpt = orderService.getOrderById(id);
            
            if (orderOpt.isEmpty()) {
                log.warn("Commande #{} non trouvée", id);
                redirectAttributes.addFlashAttribute("error", "Commande non trouvée");
                return "redirect:/mes-commandes";
            }
            
            Order order = orderOpt.get();
            
            // Vérifier que la commande appartient bien à l'utilisateur connecté
            if (!order.getUser().getId().equals(currentUser.getId())) {
                log.warn("Tentative d'accès non autorisé à la commande #{} par {}", id, currentUser.getEmail());
                redirectAttributes.addFlashAttribute("error", "Vous n'êtes pas autorisé à voir cette commande");
                return "redirect:/mes-commandes";
            }
            
            // // Vérifier et charger les produits si nécessaire
            // if (order.getOrderItems() != null) {
            //     for (OrderItem item : order.getOrderItems()) {
            //         Product product =item.getProduct();
                    
            //     }
            // }
            
            model.addAttribute("orders",List.of(order));
            log.info("Affichage des détails de la commande #{} pour {}", id, currentUser.getEmail());
            
            return "shop/mes-commandes";
            
        } catch (Exception e) {
            log.error("Erreur lors de l'affichage des détails de la commande #{}: ", id, e);
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/mes-commandes";
        }
    }
    
    // Annuler une commande
    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth == null || !auth.isAuthenticated()) {
                redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter");
                return "redirect:/login";
            }
            
            User currentUser = getCurrentUser(auth);
            
            if (currentUser == null) {
                redirectAttributes.addFlashAttribute("error", "Erreur de session utilisateur");
                return "redirect:/login";
            }
            
            Optional<Order> orderOpt = orderService.getOrderById(id);
            
            if (orderOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Commande non trouvée");
                return "redirect:/mes-commandes";
            }
            
            Order order = orderOpt.get();
            
            // Vérifier que la commande appartient à l'utilisateur
            if (!order.getUser().getId().equals(currentUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "Vous n'êtes pas autorisé à annuler cette commande");
                return "redirect:/mes-commandes";
            }
            
            // Vérifier que la commande peut être annulée
            if (!order.getStatus().equals("CONFIRMED") && !order.getStatus().equals("PENDING")) {
                redirectAttributes.addFlashAttribute("error", "Cette commande ne peut plus être annulée");
                return "redirect:/mes-commandes";
            }
            
            // Annuler la commande
            orderService.cancelOrder(id);
            
            redirectAttributes.addFlashAttribute("success", 
                "✅ Commande #" + id + " annulée avec succès!");
            
            log.info("Commande #{} annulée par {}", id, currentUser.getEmail());
            
        } catch (Exception e) {
            log.error("Erreur lors de l'annulation de la commande #{}: ", id, e);
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'annulation: " + e.getMessage());
        }
        
        return "redirect:/mes-commandes";
    }
    
    // Méthode utilitaire pour récupérer l'utilisateur courant
    private User getCurrentUser(Authentication auth) {
        if (auth == null) {
            return null;
        }
        
        Object principal = auth.getPrincipal();
        
        if (principal instanceof User) {
            return (User) principal;
        } 
        else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            Optional<User> userOpt = userRepository.findByEmail(email);
            return userOpt.orElse(null);
        }
        
        return null;
    }
}