package com.example.twitter.entities;

import jakarta.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentID;

    private String commentBody;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // Getters
    public int getCommentID() {
        return commentID;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

    // Setters
    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
