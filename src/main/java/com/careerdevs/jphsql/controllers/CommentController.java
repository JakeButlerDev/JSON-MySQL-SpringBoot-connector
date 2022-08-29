package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.CommentModel;
import com.careerdevs.jphsql.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/comments";

    @Autowired
    private CommentRepository commentRepository;

    // Getting all comments from JPH
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllCommentsAPI(RestTemplate restTemplate) {
        try {
            CommentModel[] allComments = restTemplate.getForObject(JPH_API_URL, CommentModel[].class);

            return ResponseEntity.ok(allComments);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // GET one user by id from SQL db
    @GetMapping("/sql/id/{id}")
    public ResponseEntity<?> getOneCommentById(@PathVariable String id) {
        try {
            int commentId = Integer.parseInt(id);

            System.out.println("Getting comment with ID: " + id);

            Optional<CommentModel> foundComment = commentRepository.findById(commentId);

            if (foundComment.isEmpty()) { return ResponseEntity.status(404).body("Comment NOT FOUND with ID: " + id); }

            return ResponseEntity.ok(foundComment.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number.");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Comment Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // GET all comments stored in SQL db
    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllCommentsSQL() {
        try {
            ArrayList<CommentModel> allComments = (ArrayList<CommentModel>) commentRepository.findAll();

            return ResponseEntity.ok(allComments);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // POST all comments to SQL db
    @PostMapping("/all")
    public ResponseEntity<?> uploadAllCommentDataToSQL(RestTemplate restTemplate) {
        try {
            CommentModel[] allComments = restTemplate.getForObject(JPH_API_URL, CommentModel[].class);

            assert allComments != null;

            for (CommentModel allComment : allComments) { allComment.removeId(); }
            List<CommentModel> savedComments = commentRepository.saveAll(Arrays.asList(allComments));

            return ResponseEntity.ok(savedComments);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Create comment and POST to SQL db
    @PostMapping
    public ResponseEntity<?> uploadOneComment (@RequestBody CommentModel newCommentData) {
        try {
            CommentModel savedComment = commentRepository.save(newCommentData);

            return ResponseEntity.ok(savedComment);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // DELETE one comment with id from SQL db
    @DeleteMapping("/sql/id/{id}")
    public ResponseEntity<?> deleteOneCommentById(@PathVariable String id) {
        try {
            int commentId = Integer.parseInt(id);

            System.out.println("Deleting comment with ID: " + id);

            Optional<CommentModel> foundComment = commentRepository.findById(commentId);

            if (foundComment.isEmpty()) return ResponseEntity.status(404).body("Comment Not Found With ID: " + id);

            commentRepository.deleteById(commentId);

            return ResponseEntity.ok(foundComment.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Comment Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // DELETE all comments from SQL db
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllCommentsSQL() {
        try {
            long count = commentRepository.count();
            commentRepository.deleteAll();

            return ResponseEntity.ok("Deleted comments: " + count);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //TODO: PUT one comment - alter data already inside and pass in and save new data
}
