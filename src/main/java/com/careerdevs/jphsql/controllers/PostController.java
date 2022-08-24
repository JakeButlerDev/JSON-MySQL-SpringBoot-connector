package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.PostModel;
import com.careerdevs.jphsql.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final String JPI_API_URL = "https://jsonplaceholder.typicode.com/posts";

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllPostsAPI (RestTemplate restTemplate) {
        try {

            PostModel[] allPosts = restTemplate.getForObject(JPI_API_URL, PostModel[].class);

            return ResponseEntity.ok(allPosts);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllPostsSQL() {
        try {

            ArrayList<PostModel> allPosts = (ArrayList<PostModel>) postRepository.findAll();

            return ResponseEntity.ok(allPosts);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/all")
    public ResponseEntity<?> uploadAllPostsToSQL(RestTemplate restTemplate) {
        try {
            PostModel[] allPosts = restTemplate.getForObject(JPI_API_URL, PostModel[].class);

            assert allPosts != null;
            List<PostModel> savedPosts = postRepository.saveAll(Arrays.asList(allPosts));

            return ResponseEntity.ok(savedPosts);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> uploadOnePost (@RequestBody PostModel newPostData) {
        try {
            // newPostData.removeId(); needed? Check when testing responses
            PostModel savedPost = postRepository.save(newPostData);

            return ResponseEntity.ok(savedPost);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
