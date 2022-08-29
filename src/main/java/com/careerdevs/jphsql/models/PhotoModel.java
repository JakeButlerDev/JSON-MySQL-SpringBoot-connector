package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name = "photo")
public class PhotoModel {

    //    {
//        "albumId": 1,
//            "id": 1,
//            "title": "accusamus beatae ad facilis cum similique qui sunt",
//            "url": "https://via.placeholder.com/600/92c952",
//            "thumbnailUrl": "https://via.placeholder.com/150/92c952"
//    },

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String albumId;
    private String title;
    private String url;
    private String thumbnailUrl;

    public int getId() {
        return id;
    }

    public void removeId() {
        id = 0;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
