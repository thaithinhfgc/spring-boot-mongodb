package com.example.mongodb.service;

import com.example.mongodb.exception.TodoCollectionException;
import com.example.mongodb.model.TodoDTO;
import com.example.mongodb.repository.TodoRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    private TodoRepository todoRepository;

    @Override
    public void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException {
        Optional<TodoDTO> todoFromDb = todoRepository.findByTodo(todo.getTodo());
        if (todoFromDb.isPresent()) {
            throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
        } else {
            todo.setCreateAt(new Date(System.currentTimeMillis()));
            todoRepository.save(todo);
        }
    }

    @Override
    public List<TodoDTO> getAllTodos() {
        List<TodoDTO> todos = todoRepository.findAll();
        if (todos.size() > 0) {
            return todos;
        } else {
            return new ArrayList<TodoDTO>();
        }
    }

    @Override
    public TodoDTO getTodo(String id) throws TodoCollectionException {
        Optional<TodoDTO> todo = todoRepository.findById(id);
        if (!todo.isPresent()) {
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        } else {
            return todo.get();
        }
    }

    @Override
    public void updateTodo(String id, TodoDTO todo) throws TodoCollectionException {
        Optional<TodoDTO> todoFromDb = todoRepository.findById(id);
        Optional<TodoDTO> todoWithSameName = todoRepository.findByTodo(todo.getTodo());
        if (todoFromDb.isPresent()) {
            if (todoWithSameName.isPresent() && !todoWithSameName.get().getId().equals(id)) {
                throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
            }
            TodoDTO todoUpdate = todoFromDb.get();
            todoUpdate.setTodo(todo.getTodo());
            todoUpdate.setDescription(todo.getDescription());
            todoUpdate.setCompleted(todo.getCompleted());
            todoUpdate.setUpdateAt(new Date(System.currentTimeMillis()));
            todoRepository.save(todoUpdate);
        } else {
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void deleteTodoById(String id) throws TodoCollectionException {
        Optional<TodoDTO> todo = todoRepository.findById(id);
        if (!todo.isPresent()) {
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        } else {
            todoRepository.deleteById(id);
        }
    }
}
