package com.monapp.demo.service;



import com.monapp.demo.Entity.Comment;
import com.monapp.demo.repository.CommentRepository;
import lombok.*;
import java.util.*;
@Data
@Getter@Setter
@RequiredArgsConstructor

public class CommentService {
    private final CommentRepository commentRepository;
    public Comment saveComment(Comment comment){
        return commentRepository.save(comment);
    }
    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }
    public void deleteComment(Long id){
        commentRepository.deleteById(id);
    }


}

