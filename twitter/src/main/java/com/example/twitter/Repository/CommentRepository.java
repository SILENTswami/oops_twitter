package com.example.twitter.Repository;

import com.example.twitter.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.twitter.entities.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // Add custom query methods if needed
}