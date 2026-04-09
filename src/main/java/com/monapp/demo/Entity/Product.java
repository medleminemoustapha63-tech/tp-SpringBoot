package com.monapp.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;

    public Product(){}
    public Product(String name,String description,double price){
        this.name=name;
        this.description=description;
        this.price=price;
    }
    // public Long getId(){
    //     return id;
    // }
    // public void setId(Long id){
    //     this.id=id;
    // }
    // public String getName(){
    //     return name;
    // }
    // public void setName(){
    //     this.name=name;
    //  }
    // public String getDescription(){
    //     return description;
    // }
    // public void setDescription(){
    //     this.description=description;
    //  }
    //  public double getPrice(){
    //     return price;
    //  }
    //  public void setPrice(){
    //     this.price=price;
    //  }



    
}
