package com.example.twitter.Repository;

import com.example.twitter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.twitter.entities.*;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmailAndPassword(String email, String password);
    // Add custom query methods if needed
    boolean existsByEmail(String email);
}