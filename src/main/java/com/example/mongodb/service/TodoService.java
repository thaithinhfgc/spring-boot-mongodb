package com.example.mongodb.service;

import com.example.mongodb.exception.TodoCollectionException;
import com.example.mongodb.model.TodoDTO;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

public interface TodoService {
    public void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException;

    public List<TodoDTO> getAllTodos();

    public TodoDTO getTodo(String id) throws TodoCollectionException;

    public void updateTodo(String id, TodoDTO todo) throws TodoCollectionException;

    public void deleteTodoById(String id) throws TodoCollectionException;
}
