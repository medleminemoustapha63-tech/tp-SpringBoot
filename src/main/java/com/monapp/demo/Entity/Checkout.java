package com.monapp.demo.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Checkout {
    
    // Informations personnelles
    private String fullName;        // Nom complet
    private String email;           // Email
    private String phone;           // Téléphone
    
    // Adresse de livraison
    private String address;         // Adresse
    private String postalCode;      // Code postal
    private String city;            // Ville
    
    // Paiement
    private String paymentMethod;   // Mode de paiement
    
    // Commandes
    private Long productId;         // ID du produit
    private int quantity;           // Quantité
    private String specialInstructions; // Instructions spéciales
}
    

