package com.example.twitter.Service;
import com.example.twitter.entities.Comment;
import com.example.twitter.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // Method to create a new comment
    public Comment createComment(Comment comment) {
        // Add validation logic if needed
        return commentRepository.save(comment);
    }

    // Method to retrieve a comment by ID
    public Comment getCommentById(int commentID) {
        // Add logic to fetch comment details from the repository
        return commentRepository.findById(commentID).orElse(null);
    }

    // Method to edit an existing comment
    public Comment editComment(Comment comment) {
        // Add validation and update logic
        return commentRepository.save(comment);
    }

    // Method to delete a comment
    public void deleteComment(int commentID) {
        // Add logic to delete a comment by ID
        commentRepository.deleteById(commentID);
    }


}
