package com.monapp.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.monapp.demo.Entity.Comment;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByArticleId(Long articleId);
    
    List<Comment> findByAuthor(String author);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.article.id = :articleId")
    void deleteByArticleId(@Param("articleId") Long articleId);
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.article.id = :articleId")
    long countByArticleId(@Param("articleId") Long articleId);
}