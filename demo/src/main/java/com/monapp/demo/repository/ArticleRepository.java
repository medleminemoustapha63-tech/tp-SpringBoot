package com.monapp.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monapp.demo.Entity.Article;

public interface  ArticleRepository extends JpaRepository<Article, 
Long> {    
    
}
