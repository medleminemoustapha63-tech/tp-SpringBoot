package com.monapp.demo.controller;

import com.monapp.demo.Entity.Product;
import com.monapp.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Afficher la liste
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/list";
    }

    // Afficher le détail d'un produit (lien "Voir")
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Product product = productService.getById(id);
        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Produit introuvable");
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "products/view";   // ← il faudra créer cette vue (ou rediriger vers list)
    }

    // Formulaire de création
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/form";
    }

    // Formulaire d'édition
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Product product = productService.getById(id);
        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Produit introuvable");
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "products/form";
    }

    // Création (POST /products)
    @PostMapping
    public String createProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("success", "Produit créé avec succès");
        return "redirect:/products";
    }

    // Mise à jour (POST /products/{id})
    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product,
                                RedirectAttributes redirectAttributes) {
        product.setId(id);
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("success", "Produit mis à jour");
        return "redirect:/products";
    }

    // Suppression (POST /products/{id}/delete)
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("success", "Produit supprimé");
        return "redirect:/products";
    }

    // API JSON (optionnelle)
    @GetMapping("/api/{id}")
    @ResponseBody
    public Product getProductJson(@PathVariable Long id) {
        return productService.getById(id);
    }
}