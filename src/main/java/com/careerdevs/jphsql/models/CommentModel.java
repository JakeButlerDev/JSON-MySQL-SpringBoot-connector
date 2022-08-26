package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name = "Comment")
public class CommentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String postId;
    private String name;
    private String email;

    @Column(length = 350)
    private String body;

    public int getId() {
        return id;
    }

    public void removeId() {
        id = 0;
    }

    public String getPostId() {
        return postId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBody() {
        return body;
    }

    //    {
//        "postId": 1,
//            "id": 1,
//            "name": "id labore ex et quam laborum",
//            "email": "Eliseo@gardner.biz",
//            "body": "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium"
//    },
}
