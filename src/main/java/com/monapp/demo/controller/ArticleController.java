package com.monapp.demo.controller;

import com.monapp.demo.Entity.Article;
import com.monapp.demo.Entity.Comment;
import com.monapp.demo.service.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import lombok.*;


@RestController
@RequestMapping("/api/articles")
@Service
@Data
public class ArticleController {
    private final ArticleService articleService;
    public ArticleController(ArticleService articleService){
      this.articleService=articleService;
    }
    //create Product
    @PostMapping
    public Article createArticle(@RequestBody Article article){
        return articleService.saveArticle(article);
    }
    //Get all product
    @GetMapping
    public List<Article>getAllArticles(){
        return articleService.getAllArticles();
    }
    //Get product by id
    @GetMapping("/{id}")
    public Article getById(@PathVariable Long id ){
        return articleService.getById(id);
    }
    //Delete
    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id){
        articleService.deleteArticle(id);

    }
     @PostMapping("/{id}/comments")
     public Comment addComment(@PathVariable Long id, @RequestBody Comment comment) {
        return articleService.addComment(id, comment);
     }
}
    
    

