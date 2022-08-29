package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.AlbumModel;
import com.careerdevs.jphsql.repositories.AlbumRepository;
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
@RequestMapping("/api/albums")
public class AlbumController {

    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/albums";

    @Autowired
    private AlbumRepository albumRepository;

    // Get all albums from API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllAlbumsAPI(RestTemplate restTemplate) {
        try {
            AlbumModel[] allAlbums = restTemplate.getForObject(JPH_API_URL, AlbumModel[].class);

            return ResponseEntity.ok(allAlbums);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // GET one album by id from SQL db
    @GetMapping("/sql/id/{id}")
    public ResponseEntity<?> getOneAlbumById(@PathVariable String id) {
        try {
            int albumId = Integer.parseInt(id);

            System.out.println("Getting album with ID: " + id);

            Optional<AlbumModel> foundAlbum = albumRepository.findById(albumId);

            if (foundAlbum.isEmpty()) return ResponseEntity.status(404).body("Album Not Found With ID: " + id);

            return ResponseEntity.ok(foundAlbum.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Album Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Get all albums from SQL database
    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllAlbumsSQL() {
        try {
            ArrayList<AlbumModel> allAlbums = (ArrayList<AlbumModel>) albumRepository.findAll();

            return ResponseEntity.ok(allAlbums);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Upload all Album data to SQL database
    @PostMapping("/all")
    public ResponseEntity<?> uploadAllAlbumDataToSQL(RestTemplate restTemplate) {
        try {
            AlbumModel[] allAlbums = restTemplate.getForObject(JPH_API_URL, AlbumModel[].class);

            assert allAlbums != null;
            List<AlbumModel> savedAlbums = albumRepository.saveAll(Arrays.asList(allAlbums));

            return ResponseEntity.ok(allAlbums);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // POST data of newly created album to SQL database
    @PostMapping
    public ResponseEntity<?> uploadOneAlbum(@RequestBody AlbumModel newAlbumData) {
        try {
            AlbumModel savedAlbum = albumRepository.save(newAlbumData);

            return ResponseEntity.ok(savedAlbum);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // DELETE one album by id from SQL db
    @DeleteMapping("/sql/id/{id}")
    public ResponseEntity<?> deleteOneAlbumById(@PathVariable String id) {
        try {
            int albumId = Integer.parseInt(id);

            System.out.println("Deleting album with ID: " + id);

            Optional<AlbumModel> foundAlbum = albumRepository.findById(albumId);

            if (foundAlbum.isEmpty()) return ResponseEntity.status(404).body("Album Not Found With ID: " + id);

            albumRepository.deleteById(albumId);

            return ResponseEntity.ok(foundAlbum.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Album Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // DELETE all albums from SQL db
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllAlbumsSQL() {
        try {
            long count = albumRepository.count();
            albumRepository.deleteAll();

            return ResponseEntity.ok("Deleted albums: " + count);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //TODO: PUT one album by id, overwrite data stored with new data passed in

}
