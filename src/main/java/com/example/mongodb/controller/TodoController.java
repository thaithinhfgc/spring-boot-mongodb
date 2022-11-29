package com.example.mongodb.controller;

import com.example.mongodb.exception.TodoCollectionException;
import com.example.mongodb.model.TodoDTO;
import com.example.mongodb.repository.TodoRepository;
import com.example.mongodb.service.TodoService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/todo")
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private TodoService todoService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTodo(@PathVariable("id") String id) {
        try {
            return new ResponseEntity<>(todoService.getTodo(id), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTodos() {
        List<TodoDTO> todos = todoService.getAllTodos();
        return new ResponseEntity<>(todos, todos.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todo) {
        try {
            todoService.createTodo(todo);
            return new ResponseEntity<TodoDTO>(todo, HttpStatus.OK);
        } catch (ConstraintViolationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TodoCollectionException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable("id") String id, @RequestBody TodoDTO todo) {
        try {
            todoService.updateTodo(id, todo);
            return new ResponseEntity<>("Update todo with id: " + id, HttpStatus.OK);
        } catch (ConstraintViolationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TodoCollectionException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") String id) {
        try {
            todoService.deleteTodoById(id);
            return new ResponseEntity<>("Delete todo successfully", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
