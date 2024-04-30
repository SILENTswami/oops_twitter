package com.example.twitter.Service;

import com.example.twitter.entities.*;


import com.example.twitter.entities.Post;
import com.example.twitter.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Method to create a new post
    public Post createPost(Post post) {
        // Add validation logic if needed
        post.setDate(new Date());
        return postRepository.save(post);
    }

    // Method to retrieve a post by ID
    public Post getPostById(int postID) {
        // Add logic to fetch post details from the repository
        return postRepository.findById(postID).orElse(null);
    }

    // Method to edit an existing post
    public Post editPost(Post post) {
        // Add validation and update logic
        return postRepository.save(post);
    }

    // Method to delete a post
    public void deletePost(int postID) {
        // Add logic to delete a post by ID
        postRepository.deleteById(postID);
    }

    // Add other methods as needed
    public List<Post> getAllPostsSortedByDate() {
        return postRepository.findAllByOrderByDateDesc();
    }
}
