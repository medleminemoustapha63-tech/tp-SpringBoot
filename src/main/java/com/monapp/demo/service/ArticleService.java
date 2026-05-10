package com.monapp.demo.service;

import com.monapp.demo.Entity.Article;
import com.monapp.demo.Entity.Comment;
import com.monapp.demo.repository.ArticleRepository;
import com.monapp.demo.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class ArticleService {
    
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    
    @Transactional
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }
    
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }
    
    public Article getById(Long id) {
        return articleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Article non trouvé avec l'id: " + id));
    }
    
    @Transactional
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new RuntimeException("Article non trouvé avec l'id: " + id);
        }
        articleRepository.deleteById(id);
    }
    
    @Transactional
    public Comment addComment(Long articleId, Comment comment) {
        Article article = getById(articleId);
        
        if (comment.getAuthor() == null || comment.getAuthor().trim().isEmpty()) {
            comment.setAuthor("Anonyme");
        }
        
        comment.setArticle(article);
        Comment savedComment = commentRepository.save(comment);
        article.getComments().add(savedComment);
        
        return savedComment;
    }
    
    public List<Comment> getCommentsByArticle(Long articleId) {
        Article article = getById(articleId);
        return article.getComments();
    }
    
    @Transactional
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Commentaire non trouvé avec l'id: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }
    
    @Transactional
    public Article updateArticle(Long id, Article updatedArticle) {
        Article existingArticle = getById(id);
        existingArticle.setTitle(updatedArticle.getTitle());
        existingArticle.setContent(updatedArticle.getContent());
        return articleRepository.save(existingArticle);
    }
    
    public Article getArticleWithComments(Long id) {
        return articleRepository.findArticleWithComments(id);
    }
}