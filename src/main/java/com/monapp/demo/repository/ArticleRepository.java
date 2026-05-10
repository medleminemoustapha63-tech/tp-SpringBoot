package com.monapp.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.monapp.demo.Entity.Article;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    List<Article> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT DISTINCT a FROM Article a LEFT JOIN FETCH a.comments WHERE a.id = :id")
    Article findArticleWithComments(@Param("id") Long id);
    
    @Query("SELECT COUNT(a) FROM Article a")
    long countArticles();
}