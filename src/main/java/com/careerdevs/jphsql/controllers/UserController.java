package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/users";

    @Autowired
    private UserRepository userRepository;

    // Getting all users directly from JPH API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllUsersAPI (RestTemplate restTemplate) {

        try {
            UserModel[] allUsers = restTemplate.getForObject(JPH_API_URL, UserModel[].class);

            return ResponseEntity.ok(allUsers);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    // Getting all users stored in SQL database
     @GetMapping("/sql/all")
    public ResponseEntity<?> getAllUsersSQL() {
         try {
             ArrayList<UserModel> allUsers = (ArrayList<UserModel>) userRepository.findAll();

             return ResponseEntity.ok(allUsers);

         } catch (Exception e) {
             System.out.println(e.getClass());
             System.out.println(e.getMessage());
             return ResponseEntity.internalServerError().body(e.getMessage());
         }
     }

    @PostMapping("/all")
    public ResponseEntity<?> uploadAllUserDataToSQL(RestTemplate restTemplate) {

        try {
            UserModel[] allUsers = restTemplate.getForObject(JPH_API_URL, UserModel[].class);

            //TODO: remove id from each user

            assert allUsers != null;
            List<UserModel> savedUsers = userRepository.saveAll(Arrays.asList(allUsers));

            return ResponseEntity.ok(savedUsers);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Create a new user and Post to our SQL db
    @PostMapping
    public ResponseEntity<?> uploadOneUser (@RequestBody UserModel newUserData) {

        try {
            newUserData.removeId();

            //TODO: Data validation on the new user data (make sure fields are valid)

            UserModel savedUser = userRepository.save(newUserData);


            return ResponseEntity.ok(savedUser);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //TODO: GET one user by id (from SQL database)

    // Getting one user with id from SQL database
    @GetMapping("/sql/id/{id}")
    public ResponseEntity<?> getOneUserByIdSQL(@PathVariable int id) {
        try {
            if (userRepository.existsById(id)) {
                Optional<UserModel> oneUser = userRepository.findById(id);

                return ResponseEntity.ok(oneUser);
            } else {
                return ResponseEntity.status(400).body("ID " + id + " was not found in the database. Must be a whole number.");
            }

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    //TODO: DELETE one user by id (from SQL) - must make sure a user with the given id exists

    // Deleting one user with id from SQL database
    @DeleteMapping("/sql/id/{id}")
    public ResponseEntity<?> deleteOneUserByIdSQL(@PathVariable int id) {
        try {
            if (userRepository.existsById(id)) {
                Optional<UserModel> deletedUser = userRepository.findById(id);
                userRepository.deleteById(id);
                return ResponseEntity.ok("This user has been deleted: " + deletedUser + ".");   // Currently deletedUser is a reference in memory, need to pull actual data and return
            }
            return ResponseEntity.ok("Thumbs up.");
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    //TODO: DELETE all users (from SQL) - returns how many users were deleted

    // Deleting all users from SQL db
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllUsersSQL() {
        try {
            long numberOfDeletedUsers = userRepository.count();
            userRepository.deleteAll(); //This method returns void, can we write an override method in UserRepository??

            return ResponseEntity.ok(numberOfDeletedUsers + " users have been successfully deleted.");

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    //TODO: PUT one user by id (from SQL) - must make sure a user with the given id exists

    // Still need to test this route
    //PUT one user already in SQL database
    @PutMapping("/sql/id/{id}")
    public ResponseEntity<?> updateOneUserSQL(@RequestBody UserModel updatedUserData) {
        try {
            if (userRepository.existsById(updatedUserData.getId())) {
//                    userRepository.
                return ResponseEntity.ok("Data accepted, user with id " + updatedUserData.getId() + " altered in database.");
            } else {
                return ResponseEntity.status(400).body("User with id " + updatedUserData.getId() + " not found. ID must be an integer.");
            }

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //BONUS: Add address and company to UserModel

}
