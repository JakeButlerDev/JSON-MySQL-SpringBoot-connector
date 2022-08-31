package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.UserRepository;
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
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3500")
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

    // GET one user by id (from SQL database)
    // Getting one user with id from SQL database - MY METHOD
//    @GetMapping("/sql/id/{id}")
//    public ResponseEntity<?> getOneUserByIdSQL(@PathVariable int id) {
//        try {
//            if (userRepository.existsById(id)) {
//                Optional<UserModel> oneUser = userRepository.findById(id);
//
//                return ResponseEntity.ok(oneUser);
//            } else {
//                return ResponseEntity.status(400).body("ID " + id + " was not found in the database. Must be a whole number.");
//            }
//
//        } catch (Exception e) {
//            System.out.println(e.getClass());
//            System.out.println(e.getMessage());
//            return ResponseEntity.internalServerError().body(e.getMessage());
//        }
//    }

    // Getting one user with id from SQL database - GABE METHOD
    @GetMapping("/sql/id/{id}")
    public ResponseEntity<?> getOneUserById(@PathVariable String id) {
        try {

            // throws NumberFormatException if id is not a int
            int userId = Integer.parseInt(id);

            System.out.println("Getting User With ID: " + id);

            // Get data from SQL
            Optional<UserModel> foundUser = userRepository.findById(userId);

            // One option for checking if user exists
            if (foundUser.isEmpty()) return ResponseEntity.status(404).body("User Not Found With ID: " + id);
            // Another option, throw an error if user does not exist
//            if (foundUser.isEmpty()) throw new HttpClientErrorException(HttpStatus.NOT_FOUND);

            return ResponseEntity.ok(foundUser.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("User Not Found With ID: " + id);

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

     // POST all users to SQL db
    @PostMapping("/sql/all")
    public ResponseEntity<?> uploadAllUserDataToSQL(RestTemplate restTemplate) {

        try {
            UserModel[] allUsers = restTemplate.getForObject(JPH_API_URL, UserModel[].class);

            assert allUsers != null;


            // One option, a for each loop
            for (UserModel user : allUsers) { user.removeId(); }

            //TODO: remove id from each user, find another way?

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


    // DELETE one user by id (from SQL) - must make sure a user with the given id exists
    // Deleting one user with id from SQL database - MY METHOD
//    @DeleteMapping("/sql/id/{id}")
//    public ResponseEntity<?> deleteOneUserByIdSQL(@PathVariable int id) {
//        try {
//            if (userRepository.existsById(id)) {
//                Optional<UserModel> deletedUser = userRepository.findById(id);
//                userRepository.deleteById(id);
//                return ResponseEntity.ok("This user has been deleted: " + deletedUser.get() + ".");   // Currently deletedUser is a reference in memory, need to pull actual data and return
//            }
//            return ResponseEntity.ok("Thumbs up.");
//        } catch (Exception e) {
//            System.out.println(e.getClass());
//            System.out.println(e.getMessage());
//            return ResponseEntity.internalServerError().body(e.getMessage());
//        }
//    }

    // Deleting one user with id from SQL database - GABE METHOD
    @DeleteMapping("/sql/id/{id}")
    public ResponseEntity<?> deleteOneUserById(@PathVariable String id) {
        try {

            // throws NumberFormatException if id is not a int
            int userId = Integer.parseInt(id);

            System.out.println("Deleting User With ID: " + id);

            // Get data from SQL
            Optional<UserModel> foundUser = userRepository.findById(userId);

            // One option for checking if user exists
            if (foundUser.isEmpty()) return ResponseEntity.status(404).body("User Not Found With ID: " + id);
            // Another option, throw an error if user does not exist
//            if (foundUser.isEmpty()) throw new HttpClientErrorException(HttpStatus.NOT_FOUND);

            userRepository.deleteById(userId);

            return ResponseEntity.ok(foundUser.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("User Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // DELETE all users (from SQL) - returns how many users were deleted
    // Deleting all users from SQL db - MY METHOD
//    @DeleteMapping("/sql/all")
//    public ResponseEntity<?> deleteAllUsersSQL() {
//        try {
//            long numberOfDeletedUsers = userRepository.count();
//            userRepository.deleteAll(); //This method returns void, can we write an override method in UserRepository if we want to return deleted data??
//
//            return ResponseEntity.ok(numberOfDeletedUsers + " users have been successfully deleted.");
//
//        } catch (Exception e) {
//            System.out.println(e.getClass());
//            System.out.println(e.getMessage());
//            return ResponseEntity.internalServerError().body(e.getMessage());
//        }
//    }

    // Deleting all users from SQL db - GABE METHOD
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllUsersSQL() {
        try {
            long count = userRepository.count();
            userRepository.deleteAll();

            return ResponseEntity.ok("Deleted users: " + count);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    //PUT one user by id (from SQL) - must make sure a user with the given id exists
    // Still need to test this route
    //PUT one user already in SQL database - MY METHOD UNFINISHED
//    @PutMapping("/sql/id/{id}")
//    public ResponseEntity<?> updateOneUserSQL(@RequestBody UserModel updatedUserData) {
//        try {
//            int userId = updatedUserData.getId();
//
//            userRepository.findById(userId);
//
//            UserModel editedUser = userRepository.save(updatedUserData);
//
//            return ResponseEntity.ok(editedUser);
//
//        } catch (Exception e) {
//            System.out.println(e.getClass());
//            System.out.println(e.getMessage());
//            return ResponseEntity.internalServerError().body(e.getMessage());
//        }
//    }

    // PUT one user already in SQL database - GABE METHOD
    @PutMapping("/sql/id/{id}")
    public ResponseEntity<?> updateOneUserSQL(@PathVariable String id, @RequestBody UserModel updateUserData) {
        try {
            int userId = Integer.parseInt(id);

            Optional<UserModel> foundUser = userRepository.findById(userId);

            if (foundUser.isEmpty()) return ResponseEntity.status(404).body("User Not Found With ID: " + id);

            if (userId != updateUserData.getId()) return ResponseEntity.status(400).body("User IDs did not match!");

            userRepository.save(updateUserData);

            return ResponseEntity.ok(updateUserData);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
