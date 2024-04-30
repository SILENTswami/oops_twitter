package com.example.twitter.Service;

import com.example.twitter.entities.User;
import com.example.twitter.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.twitter.entities.*;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Method to register a new user
    public User registerUser(User user) {
        // Add validation logic if needed
        return userRepository.save(user);
    }

    // Method to authenticate a user
    public boolean authenticateUser(String email, String password) {
        // Add authentication logic here
        return userRepository.existsByEmailAndPassword(email, password);
    }

    // Method to fetch user details by ID
    public User getUserDetailsById(int userID) {
        // Add logic to fetch user details from the repository
        return userRepository.findById(userID).orElse(null);
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Method to check if a user exists by ID
    public boolean userExistsById(int userID) {
        return userRepository.existsById(userID);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}