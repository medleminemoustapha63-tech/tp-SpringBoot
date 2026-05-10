package com.monapp.demo.service;

import com.monapp.demo.Entity.Comment;
import com.monapp.demo.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
    
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));
    }
    
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
    
    public List<Comment> getCommentsByArticle(Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }
    
    public List<Comment> getCommentsByAuthor(String author) {
        return commentRepository.findByAuthor(author);
    }
}