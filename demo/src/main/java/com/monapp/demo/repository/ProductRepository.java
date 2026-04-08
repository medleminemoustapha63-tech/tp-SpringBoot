package com.monapp.demo.repository;
import com.monapp.demo.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
public interface  ProductRepository extends JpaRepository<Product, 
Long> {    
}
