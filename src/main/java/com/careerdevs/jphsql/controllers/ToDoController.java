package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.PostModel;
import com.careerdevs.jphsql.models.ToDoModel;
import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.ToDoRepository;
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
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3500")
public class ToDoController {

    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/todos";

    @Autowired
    private ToDoRepository toDoRepository;

    // Get all Todos from API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllTodosAPI(RestTemplate restTemplate) {
        try {
            ToDoModel[] allTodos = restTemplate.getForObject(JPH_API_URL, ToDoModel[].class);

            return ResponseEntity.ok(allTodos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // GET one to_o with id from SQL db
    @GetMapping("/sql/id/{id}")
    public ResponseEntity<?> getOneTodoById(@PathVariable String id) {
        try {
            int todoId = Integer.parseInt(id);

            System.out.println("Getting todo with ID: " + id);

            Optional<ToDoModel> foundTodo = toDoRepository.findById(todoId);

            if (foundTodo.isEmpty()) return ResponseEntity.status(404).body("Todo Not Found With ID: " + id);

            return ResponseEntity.ok(foundTodo.get());
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

    // Get all Todos stored in SQL db
    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllTodosSQL() {
        try {
            ArrayList<ToDoModel> allTodos = (ArrayList<ToDoModel>) toDoRepository.findAll();

            return ResponseEntity.ok(allTodos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Store all API data into SQL db
    @PostMapping("/sql/all")
    public ResponseEntity<?> uploadAllTodoDataToSQL(RestTemplate restTemplate) {
        try {
            ToDoModel[] allTodos = restTemplate.getForObject(JPH_API_URL, ToDoModel[].class);

            assert allTodos != null;
            for (ToDoModel todo : allTodos) { todo.removeId(); }
            List<ToDoModel> savedTodos = toDoRepository.saveAll(Arrays.asList(allTodos));

            return ResponseEntity.ok(savedTodos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Create a new To-do and POST to SQL db
    @PostMapping
    public ResponseEntity<?> uploadOneTodo(@RequestBody ToDoModel newTodoData) {
        try {
            newTodoData.removeId();
            ToDoModel savedTodo = toDoRepository.save(newTodoData);

            return ResponseEntity.ok(savedTodo);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // DELETE one user with id from SQL db
    @DeleteMapping("/sql/id/{id}")
    public ResponseEntity<?> deleteOneTodoById(@PathVariable String id) {
        try {
            int todoId = Integer.parseInt(id);

            System.out.println("Deleting todo with ID:"  + id);

            Optional<ToDoModel> foundTodo = toDoRepository.findById(todoId);

            if (foundTodo.isEmpty()) return ResponseEntity.status(404).body("Todo Not Found With ID: " + id);

            toDoRepository.deleteById(todoId);

            return ResponseEntity.ok(foundTodo.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Todo Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // DELETE all Todos from SQL db - return how many were deleted
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllTodosSQL() {
        try {
            long count = toDoRepository.count();
            toDoRepository.deleteAll();

            return ResponseEntity.ok("Deleted todos: " + count);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //PUT one to-do already in SQL db
    @PutMapping("/sql/id/{id}")
    public ResponseEntity<?> updateOneTodoSQL(@PathVariable String id, @RequestBody ToDoModel updateTodoData) {
        try {
            int todoId = Integer.parseInt(id);

            Optional<ToDoModel> foundTodo = toDoRepository.findById(todoId);

            if (foundTodo.isEmpty()) return ResponseEntity.status(404).body("Todo Not Found With ID: " + id);

            if (todoId != updateTodoData.getId()) return ResponseEntity.status(400).body("Todo IDs did not match!");

            toDoRepository.save(updateTodoData);

            return ResponseEntity.ok(updateTodoData);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
