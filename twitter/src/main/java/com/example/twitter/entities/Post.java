//package com.example.twitter.entities;
//
//import jakarta.persistence.*;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//public class Post {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int postID;
//
//    private String postBody;
//    private Date date;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
//    private List<Comment> comments;
//
//    // Getters and setters
//}

package com.example.twitter.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postID;

    private String postBody;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    // Getters
    public int getPostID() {
        return postID;
    }

    public String getPostBody() {
        return postBody;
    }

    public Date getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    // Setters
    public void setPostID(int postID) {
        this.postID = postID;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
