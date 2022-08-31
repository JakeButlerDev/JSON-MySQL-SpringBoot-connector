package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name = "Album")
public class AlbumModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String userId;
    private String title;

    public int getId() {
        return id;
    }

    public void removeId() {
        id = 0;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    //    {
//        "userId": 1,
//            "id": 1,
//            "title": "quidem molestiae enim"
//    },
}
