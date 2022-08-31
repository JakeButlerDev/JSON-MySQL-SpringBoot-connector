package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.PhotoModel;
import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.PhotoRepository;
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
@RequestMapping("/api/photos")
@CrossOrigin(origins = "http://localhost:3500")
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

    // GET one photo by id from SQL db
    @GetMapping("/sql/id/{id}")
    public ResponseEntity<?> getOnePhotoById(@PathVariable String id) {
        try {
            int photoId = Integer.parseInt(id);

            System.out.println("Getting photo with ID: " + id);

            Optional<PhotoModel> foundPhoto = photoRepository.findById(photoId);

            if (foundPhoto.isEmpty()) return ResponseEntity.status(404).body("Photo Not Found With ID: " + id);

            return ResponseEntity.ok(foundPhoto.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Photo Not Found With ID: " + id);

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
    @PostMapping("/sql/all")
    public ResponseEntity<?> uploadAllPhotoDataToSQL(RestTemplate restTemplate) {
        try {
            PhotoModel[] allPhotos = restTemplate.getForObject(JPH_API_URL, PhotoModel[].class);

            assert allPhotos != null;
            for (PhotoModel photo : allPhotos) { photo.removeId(); }
            List<PhotoModel> savedPhotos = photoRepository.saveAll(Arrays.asList(allPhotos));

            return ResponseEntity.ok(savedPhotos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Create a new photo and post to SQL db
    @PostMapping
    public ResponseEntity<?> uploadOnePhoto(@RequestBody PhotoModel newPhotoData) {
        try {
            newPhotoData.removeId();

            PhotoModel savedPhoto = photoRepository.save(newPhotoData);

            return ResponseEntity.ok(savedPhoto);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // DELETE one photo by id from SQL db - make sure id is a valid photo first
    @DeleteMapping("/sql/id/{id}")
    public ResponseEntity<?> deleteOnePhotoById(@PathVariable String id) {
        try {
            int photoId = Integer.parseInt(id);

            System.out.println("Deleting photo with ID: " + id);

            Optional<PhotoModel> foundPhoto = photoRepository.findById(photoId);

            if (foundPhoto.isEmpty()) return ResponseEntity.status(404).body("Photo Not Found With ID: " + id);

            photoRepository.deleteById(photoId);

            return ResponseEntity.ok(foundPhoto.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Photo Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // DELETE all photos from SQL db
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllPhotosSQL() {
        try {
            long count = photoRepository.count();
            photoRepository.deleteAll();

            return ResponseEntity.ok("Deleted photos: " + count);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //PUT one photo with id already in SQL db
    @PutMapping("/sql/id/{id}")
    public ResponseEntity<?> updateOnePhotoSQL(@PathVariable String id, @RequestBody PhotoModel updatePhotoData) {
        try {
            int photoId = Integer.parseInt(id);

            Optional <PhotoModel> foundPhoto = photoRepository.findById(photoId);

            if (foundPhoto.isEmpty()) return ResponseEntity.status(404).body("Photo Not Found With ID: " + id);

            if (photoId != updatePhotoData.getId()) return ResponseEntity.status(400).body("Photo IDs did not match!");

            photoRepository.save(updatePhotoData);

            return ResponseEntity.ok(updatePhotoData);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
