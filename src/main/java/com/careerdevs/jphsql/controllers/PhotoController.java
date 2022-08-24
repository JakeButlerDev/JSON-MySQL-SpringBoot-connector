package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.PhotoModel;
import com.careerdevs.jphsql.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/photos";

    @Autowired
    private PhotoRepository photoRepository;

    // GET all photos from API
    @GetMapping("/api/all")
    public ResponseEntity<?> getAllPhotosAPI(RestTemplate restTemplate) {
        try {
            PhotoModel[] allPhotos = restTemplate.getForObject(JPH_API_URL, PhotoModel[].class);

            return ResponseEntity.ok(allPhotos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // GET all photos stored in SQL db
    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllPhotosSQL() {
        try {
            ArrayList<PhotoModel> allPhotos = (ArrayList<PhotoModel>) photoRepository.findAll();

            return ResponseEntity.ok(allPhotos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Storing all API photo data into SQL db
    @PostMapping("/all")
    public ResponseEntity<?> uploadAllPhotoDataToSQL(RestTemplate restTemplate) {
        try {
            PhotoModel[] allPhotos = restTemplate.getForObject(JPH_API_URL, PhotoModel[].class);

            assert allPhotos != null;
            List<PhotoModel> savedPhotos = photoRepository.saveAll(Arrays.asList(allPhotos));

            return ResponseEntity.ok(savedPhotos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> uploadOnePhoto(@RequestBody PhotoModel newPhotoData) {
        try {
            PhotoModel savedPhoto = photoRepository.save(newPhotoData);

            return ResponseEntity.ok(savedPhoto);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
