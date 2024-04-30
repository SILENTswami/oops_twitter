package com.example.twitter.Controller;

import com.example.twitter.entities.Comment;
import com.example.twitter.entities.Post;
import com.example.twitter.Service.CommentService;
import com.example.twitter.Service.PostService;
import com.example.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class CommentController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public CommentController(UserService userService, PostService postService, CommentService commentService) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest commentRequest) {

        // Check if the user exists
        if (!userService.userExistsById(commentRequest.getUserID())) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Check if the post exists
        Post post = postService.getPostById(commentRequest.getPostID());
        if (post == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }



        // Create the new comment
        Comment comment = new Comment();
        comment.setCommentBody(commentRequest.getCommentBody());
        comment.setPost(post);
        comment.setUser(userService.getUserDetailsById(commentRequest.getUserID()));

        // Save the comment
        commentService.createComment(comment);

        return new ResponseEntity<>("Comment created successfully", HttpStatus.CREATED);
    }



    @GetMapping("/comment")
    public ResponseEntity<?> getCommentDetails(@RequestParam("commentID") int commentID) {
        Comment comment = commentService.getCommentById(commentID);
        if (comment == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Prepare the response including comment details
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("commentID", comment.getCommentID());
        responseMap.put("commentBody", comment.getCommentBody());

        Map<String, Object> commentCreatorMap = new LinkedHashMap<>();
        commentCreatorMap.put("userID", comment.getUser().getUserID());
        commentCreatorMap.put("name", comment.getUser().getName());

        responseMap.put("commentCreator", commentCreatorMap);

        return ResponseEntity.ok(responseMap);
    }

    @PatchMapping("/comment")
    public ResponseEntity<?> editComment(@RequestBody CommentRequest commentRequest) {
        // Check if the comment exists
        Comment comment = commentService.getCommentById(commentRequest.getCommentID());
        if (comment == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Update the comment body
        comment.setCommentBody(commentRequest.getCommentBody());

        // Save the updated comment
        commentService.editComment(comment);

        return new ResponseEntity<>("Comment edited successfully", HttpStatus.OK);
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(@RequestParam("commentID") int commentID) {
        // Check if the comment exists
        Comment comment = commentService.getCommentById(commentID);
        if (comment == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Delete the comment
        commentService.deleteComment(commentID);

        return new ResponseEntity<>("Comment deleted", HttpStatus.OK);
    }



    // Inner class to represent the request body for creating a comment
    static class CommentRequest {
        private String commentBody;
        private int postID;
        private int userID;
        private int commentID;

        // Getters and setters
        public String getCommentBody() {
            return commentBody;
        }

        public void setCommentBody(String commentBody) {
            this.commentBody = commentBody;
        }

        public int getPostID() {
            return postID;
        }

        public int getCommentID() {
            return commentID;
        }

        public void setCommentID(int commentID) {
            this.commentID = commentID;
        }
        public void setPostID(int postID) {
            this.postID = postID;
        }

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }
    }
}
