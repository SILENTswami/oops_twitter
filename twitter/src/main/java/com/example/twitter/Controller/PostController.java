package com.example.twitter.Controller;

import com.example.twitter.entities.Post;
import com.example.twitter.Service.PostService;
import com.example.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class PostController {

    private final UserService userService;
    private final PostService postService;

    @Autowired
    public PostController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) {
        // Check if the user exists
        if (!userService.userExistsById(postRequest.getUserID())) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Create the new post
        Post post = new Post();
        post.setPostBody(postRequest.getPostBody());
        post.setUser(userService.getUserDetailsById(postRequest.getUserID())); // Set the user for the post

        // Set other properties as needed

        // Save the post
        postService.createPost(post);

        return new ResponseEntity<>("Post created successfully", HttpStatus.CREATED);
    }


    @GetMapping("/post")
    public ResponseEntity<?> getPostDetails(@RequestParam("postID") int postID) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Post post = postService.getPostById(postID);
        if (post == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        LocalDate localDate = post.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();;
        // Prepare the response using LinkedHashMap to ensure the order of elements
        Map<String, Object> postDetails = new LinkedHashMap<>();
        postDetails.put("postID", post.getPostID());
        postDetails.put("postBody", post.getPostBody());
        postDetails.put("date", dateFormatter.format(localDate));

        List<Map<String, Object>> commentsList = post.getComments().stream().map(comment -> {
            Map<String, Object> commentDetails = new LinkedHashMap<>();
            commentDetails.put("commentID", comment.getCommentID());
            commentDetails.put("commentBody", comment.getCommentBody());

            Map<String, Object> commentCreatorDetails = new LinkedHashMap<>();
            commentCreatorDetails.put("userID", comment.getUser().getUserID());
            commentCreatorDetails.put("name", comment.getUser().getName());

            commentDetails.put("commentCreator", commentCreatorDetails);
            return commentDetails;
        }).collect(Collectors.toList());

        postDetails.put("comments", commentsList);

        return new ResponseEntity<>(postDetails, HttpStatus.OK);
    }



    @PatchMapping("/post")
    public ResponseEntity<?> editPost(@RequestBody EditPostRequest editPostRequest) {
        int postID = editPostRequest.getPostID();
        Post existingPost = postService.getPostById(postID);
        if (existingPost == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Update the post with new details
        existingPost.setPostBody(editPostRequest.getPostBody());
        // Set other properties as needed

        // Save the updated post
        postService.editPost(existingPost);

        return new ResponseEntity<>("Post edited successfully", HttpStatus.OK);
    }



    @DeleteMapping("/post")
    public ResponseEntity<?> deletePost(@RequestParam("postID") int postID) {
        Post existingPost = postService.getPostById(postID);
        if (existingPost == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Delete the post
        postService.deletePost(postID);

        return new ResponseEntity<>("Post deleted", HttpStatus.OK);
    }



    // Inner class to represent the request body for creating a post
    static class PostRequest {
        private String postBody;
        private int userID;

        // Getters and setters
        public String getPostBody() {
            return postBody;
        }

        public void setPostBody(String postBody) {
            this.postBody = postBody;
        }

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }
    }

    // Inner class to represent the request body for editing a post
    static class EditPostRequest {
        private int postID;
        private String postBody;

        // Getters and setters
        public int getPostID() {
            return postID;
        }

        public void setPostID(int postID) {
            this.postID = postID;
        }

        public String getPostBody() {
            return postBody;
        }

        public void setPostBody(String postBody) {
            this.postBody = postBody;
        }
    }
}
