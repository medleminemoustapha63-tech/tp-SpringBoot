package com.monapp.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.monapp.demo.Entity.Product;
public interface  ProductRepository extends JpaRepository<Product, 
Long> {    
}
