package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name = "todo")
public class ToDoModel {

//    {
//        "userId": 1,
//            "id": 1,
//            "title": "delectus aut autem",
//            "completed": false
//    },

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String userId;
    private String title;
    private boolean completed;

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void removeId() {
        id = 0;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }
}
