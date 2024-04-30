package com.example.twitter.Repository;

//import com.example.twitter.entities.Post;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.example.twitter.entities.*;
//
//@Repository
//public interface PostRepository extends JpaRepository<Post, Long> {
//    // Add custom query methods if needed
//}

import com.example.twitter.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p ORDER BY p.date DESC")
    List<Post> findAllByOrderByDateDesc();
}
