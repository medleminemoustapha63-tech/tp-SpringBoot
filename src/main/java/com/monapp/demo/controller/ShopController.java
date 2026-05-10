package com.monapp.demo.controller;

import com.monapp.demo.Entity.Order;
import com.monapp.demo.Entity.Product;
import com.monapp.demo.Entity.User;
import com.monapp.demo.dto.Cart;
import com.monapp.demo.dto.CartItem;
import com.monapp.demo.repository.UserRepository;
import com.monapp.demo.service.OrderService;
import com.monapp.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ShopController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/")
    public String home() {
        return "redirect:/shop/products";
    }
    
    @GetMapping("/shop/products")
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "shop/products";
    }
    
    @PostMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable Long id, 
                           @RequestParam int quantity, 
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getById(id);
            
            if (product == null) {
                redirectAttributes.addFlashAttribute("error", "Produit non trouvé");
                return "redirect:/shop/products";
            }
            
            if (quantity > product.getStock()) {
                redirectAttributes.addFlashAttribute("error", 
                    "Stock insuffisant! Stock disponible: " + product.getStock());
                return "redirect:/shop/products";
            }
            
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
                session.setAttribute("cart", cart);
            }
            
            cart.addItem(product, quantity);
            redirectAttributes.addFlashAttribute("success", 
                "✅ " + quantity + " x " + product.getName() + " ajouté au panier!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        
        return "redirect:/shop/products";
    }
    
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        
        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("total", cart.getTotal());
        model.addAttribute("itemCount", cart.getItemCount());
        
        return "shop/cart";
    }
    
    @PostMapping("/cart/update/{id}")
    public String updateQuantity(@PathVariable Long id, 
                                @RequestParam int quantity,
                                HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.updateQuantity(id, quantity);
        }
        return "redirect:/cart";
    }
    
    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.removeItem(id);
        }
        return "redirect:/cart";
    }
    
    @GetMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.clear();
        }
        return "redirect:/cart";
    }
    
    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }
        
        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("total", cart.getTotal());
        
        return "shop/checkout";
    }
    
    private User getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
                System.out.println("❌ Pas d'authentification trouvée");
                return null;
            }
            
            Object principal = auth.getPrincipal();
            System.out.println("Principal class: " + principal.getClass().getName());
            
            if (principal instanceof User) {
                System.out.println("✅ Principal est une instance de User");
                return (User) principal;
            }
            
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                org.springframework.security.core.userdetails.UserDetails userDetails = 
                    (org.springframework.security.core.userdetails.UserDetails) principal;
                String email = userDetails.getUsername();
                System.out.println("📧 Récupération par email: " + email);
                
                Optional<User> userOpt = userRepository.findByEmail(email);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    System.out.println("✅ Utilisateur trouvé: " + user.getEmail());
                    return user;
                }
            }
            
            if (principal instanceof String) {
                String email = (String) principal;
                System.out.println("📧 Principal est une String: " + email);
                
                Optional<User> userOpt = userRepository.findByEmail(email);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    System.out.println("✅ Utilisateur trouvé: " + user.getEmail());
                    return user;
                }
            }
            
            System.out.println("❌ Impossible de récupérer l'utilisateur");
            return null;
            
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @PostMapping("/checkout")
    public String processCheckout(@RequestParam String fullName,
                                  @RequestParam String address,
                                  @RequestParam String phone,
                                  @RequestParam String paymentMethod,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== DÉBUT CHECKOUT ===");
            
            Cart cart = (Cart) session.getAttribute("cart");
            
            if (cart == null || cart.isEmpty()) {
                System.out.println("❌ Panier vide");
                redirectAttributes.addFlashAttribute("error", "Panier vide!");
                return "redirect:/cart";
            }
            
            System.out.println("📦 Panier: " + cart.getItemCount() + " articles");
            
            User user = getCurrentUser();
            if (user == null) {
                System.out.println("❌ Utilisateur non connecté");
                redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter");
                return "redirect:/login";
            }
            
            System.out.println("✅ Utilisateur: " + user.getEmail());
            
            // Vérifier les stocks
            for (CartItem item : cart.getCartItems()) {
                Product freshProduct = productService.getById(item.getProduct().getId());
                if (item.getQuantity() > freshProduct.getStock()) {
                    redirectAttributes.addFlashAttribute("error", 
                        "Stock insuffisant pour " + freshProduct.getName());
                    return "redirect:/cart";
                }
            }
            
            // Préparer la commande
            List<Product> products = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();
            
            for (CartItem item : cart.getCartItems()) {
                Product freshProduct = productService.getById(item.getProduct().getId());
                products.add(freshProduct);
                quantities.add(item.getQuantity());
                System.out.println("📝 Produit: " + freshProduct.getName() + " x" + item.getQuantity());
            }
            
            // Créer la commande
            Order order = orderService.createOrder(products, quantities, user);
            System.out.println("✅ Commande créée! ID: " + order.getId());
            
            // Vider le panier
            cart.clear();
            session.setAttribute("cart", cart);
            
            redirectAttributes.addFlashAttribute("success", 
                "✅ Commande #" + order.getId() + " confirmée! Merci " + fullName);
            
            // ✅ CORRECTION ICI - Redirection vers mes-commandes
            return "redirect:/mes-commandes";
            
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/cart";
        }
    }
}