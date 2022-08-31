package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.PostModel;
import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3500")
public class PostController {

    private final String JPI_API_URL = "https://jsonplaceholder.typicode.com/posts";

    @Autowired
    private PostRepository postRepository;

    // GET all posts from JPH API
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

    // GET one post with id from SQL db
    @GetMapping("/sql/id/{id}")
    public ResponseEntity<?> getOnePostByIdSQL(@PathVariable String id) {
        try {
            int postId = Integer.parseInt(id);

            System.out.println("Getting post with ID: " + id);

            Optional<PostModel> foundPost = postRepository.findById(postId);

            if (foundPost.isEmpty()) return ResponseEntity.status(404).body("Post Not Found With ID: " + id);

            return ResponseEntity.ok(foundPost.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Post Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // GET all posts in SQL db
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

    // POST all posts to SQL db
    @PostMapping("/sql/all")
    public ResponseEntity<?> uploadAllPostsToSQL(RestTemplate restTemplate) {
        try {
            PostModel[] allPosts = restTemplate.getForObject(JPI_API_URL, PostModel[].class);

            assert allPosts != null;
            for (PostModel post : allPosts) { post.removeId(); }
            List<PostModel> savedPosts = postRepository.saveAll(Arrays.asList(allPosts));

            return ResponseEntity.ok(savedPosts);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Create a new post and POST to SQL db
    @PostMapping
    public ResponseEntity<?> uploadOnePost (@RequestBody PostModel newPostData) {
        try {
            newPostData.removeId();
            PostModel savedPost = postRepository.save(newPostData);

            return ResponseEntity.ok(savedPost);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // DELETE one post by id from SQL db - make sure given id exists
    @DeleteMapping("/sql/id/{id}")
    public ResponseEntity<?> deleteOnePostById(@PathVariable String id) {
        try {
            int postId = Integer.parseInt(id);

            System.out.println("Deleting post with ID: " + id);

            Optional<PostModel> foundPost = postRepository.findById(postId);

            if (foundPost.isEmpty()) return ResponseEntity.status(404).body("Post Not Found With ID: " + id);

            return ResponseEntity.ok(foundPost.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Post Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // DELETE all posts from SQL db
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllPostsSQL() {
        try {
            long count = postRepository.count();
            postRepository.deleteAll();

            return ResponseEntity.ok("Deleted posts: " + count);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //PUT one post already in SQL db
    @PutMapping("/sql/id/{id}")
    public ResponseEntity<?> updateOnePostSQL(@PathVariable String id, @RequestBody PostModel updatePostData) {
        try {
            int postId = Integer.parseInt(id);

            Optional<PostModel> foundPost = postRepository.findById(postId);

            if (foundPost.isEmpty()) return ResponseEntity.status(404).body("Post Not Found With ID: " + id);

            if (postId != updatePostData.getId()) return ResponseEntity.status(400).body("Post IDs did not match!");

            postRepository.save(updatePostData);

            return ResponseEntity.ok(updatePostData);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
