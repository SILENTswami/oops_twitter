package com.example.twitter.Controller;
import com.example.twitter.Service.PostService;
import com.example.twitter.entities.Post;
import com.example.twitter.entities.User;
import com.example.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@RestController
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }



    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {


        // remove the try catch block
        try {
            if (userService.userExists(user.getEmail())) {
//                return new ResponseEntity<>("Forbidden, Account already exists", HttpStatus.FORBIDDEN);
                Map<String, String> errorResponse = new LinkedHashMap<>();
                errorResponse.put("Error", "Forbidden, Account already exists");
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            }
            userService.registerUser(user);
            return new ResponseEntity<>("Account Creation Successful", HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Database error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        // Check if the email exists first
        if (!userService.userExists(email)) {
//            return new ResponseEntity<>("User does not exist", HttpStatus.UNAUTHORIZED);
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

        }

        // Authenticate user
        if (userService.authenticateUser(email, password)) {
            return new ResponseEntity<>("Login Successful", HttpStatus.OK);
        } else {

            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Username/Password Incorrect");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@RequestParam("userID") int userID) {
        User user = userService.getUserDetailsById(userID);
        if (user == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Prepare the response including user posts and comments
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", user.getName());
        userDetails.put("userID", user.getUserID());
        userDetails.put("email", user.getEmail());

        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }


    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getUserFeed() {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Post> posts = postService.getAllPostsSortedByDate();
        List<Map<String, Object>> formattedPosts = posts.stream().map(post -> {
            Map<String, Object> postDetails = new LinkedHashMap<>();
            postDetails.put("postID", post.getPostID());
            postDetails.put("postBody", post.getPostBody());

            // Convert Date to LocalDate
            LocalDate localDate = post.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            postDetails.put("date", dateFormatter.format(localDate));

            List<Map<String, Object>> commentsList = post.getComments().stream().map(comment -> {
                Map<String, Object> commentDetails = new LinkedHashMap<>();
                commentDetails.put("commentID", comment.getCommentID());
                commentDetails.put("commentBody", comment.getCommentBody());  // Ensure this method exists and is working correctly
                Map<String, Object> commentCreatorDetails = new LinkedHashMap<>();
                commentCreatorDetails.put("userID", comment.getUser().getUserID());
                commentCreatorDetails.put("name", comment.getUser().getName());
                commentDetails.put("commentCreator", commentCreatorDetails);
                return commentDetails;
            }).collect(Collectors.toList());

            postDetails.put("comments", commentsList);
            return postDetails;
        }).toList();

        return ResponseEntity.ok(Map.of("posts", formattedPosts));
    }


    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<Map<String, Object>> userDetailsList = users.stream()
                .map(user -> {
                    LinkedHashMap<String, Object> userDetails = new LinkedHashMap<>();
                    userDetails.put("name", user.getName());
                    userDetails.put("userID", user.getUserID());
                    userDetails.put("email", user.getEmail());
                    return userDetails;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDetailsList);
    }

}

