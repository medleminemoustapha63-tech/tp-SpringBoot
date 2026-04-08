package com.monapp.demo.service;

import com.monapp.demo.Entity.Article;
import com.monapp.demo.Entity.Comment;
import com.monapp.demo.repository.ArticleRepository;
import com.monapp.demo.repository.CommentRepository;

import java.util.List;
import lombok.*;

import org.springframework.stereotype.Service;
@Service
@AllArgsConstructor
@Getter
@Setter
public class ArticleService {
    
     ArticleRepository articleRepository;
     CommentRepository commentRepository;
    
     // 🔥 إنشاء Article
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }   
    public Article saveArticle(Article article){
        return articleRepository.save(article);
    }
    public List<Article> getAllArticles(){
        return articleRepository.findAll();
    }
     public Article getById(Long id){
        return articleRepository.findById(id).orElse(null);
    }
    public void deleteArticle(Long id){
        articleRepository.deleteById(id);

    }
     // 🔥 إضافة Comment إلى Article (المهم)
    public Comment addComment(Long articleId, Comment comment) {
        Article article = getById(articleId);

        // الربط الحقيقي
        comment.setArticle(article);

        return commentRepository.save(comment);
    }
}