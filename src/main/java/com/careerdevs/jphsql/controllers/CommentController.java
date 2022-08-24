package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.CommentModel;
import com.careerdevs.jphsql.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @PostMapping("/all")
    public ResponseEntity<?> uploadAllCommentDataToSQL(RestTemplate restTemplate) {
        try {
            CommentModel[] allComments = restTemplate.getForObject(JPH_API_URL, CommentModel[].class);

            assert allComments != null;
            List<CommentModel> savedComments = commentRepository.saveAll(Arrays.asList(allComments));

            return ResponseEntity.ok(savedComments);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

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
}
