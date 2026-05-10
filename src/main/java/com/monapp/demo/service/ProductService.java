package com.monapp.demo.service;

import com.monapp.demo.Entity.Product;
import com.monapp.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public Product save(Product product) {
        return productRepository.save(product);
    }
    
    @Transactional
    public void updateProduct(Product product) {
        productRepository.save(product);
    }
}