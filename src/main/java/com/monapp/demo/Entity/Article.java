package com.monapp.demo.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "article")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("article")
    private List<Comment> comments = new ArrayList<>();
    
    // Helper method pour ajouter un commentaire
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setArticle(this);
    }
    
    // Helper method pour supprimer un commentaire
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setArticle(null);
    }
}