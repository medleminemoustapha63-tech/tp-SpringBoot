package com.monapp.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monapp.demo.Entity.Comment;

public interface  CommentRepository extends JpaRepository<Comment, 
Long> {    
    
}
