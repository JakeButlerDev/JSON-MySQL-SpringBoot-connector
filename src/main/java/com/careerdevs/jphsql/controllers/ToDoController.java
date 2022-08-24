package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.ToDoModel;
import com.careerdevs.jphsql.repositories.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
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
    @PostMapping("/all")
    public ResponseEntity<?> uploadAllTodoDataToSQL(RestTemplate restTemplate) {
        try {
            ToDoModel[] allTodos = restTemplate.getForObject(JPH_API_URL, ToDoModel[].class);

            assert allTodos != null;
            List<ToDoModel> savedTodos = toDoRepository.saveAll(Arrays.asList(allTodos));

            return ResponseEntity.ok(savedTodos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> uploadOneTodo(@RequestBody ToDoModel newTodoData) {
        try {
            ToDoModel savedTodo = toDoRepository.save(newTodoData);

            return ResponseEntity.ok(savedTodo);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
