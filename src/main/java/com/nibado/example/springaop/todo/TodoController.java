package com.nibado.example.springaop.todo;

import com.nibado.example.springaop.aspects.Restrict;
import com.nibado.example.springaop.todo.domain.TodoList;
import com.nibado.example.springaop.todo.dto.AllTodos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/todo")
@Slf4j
public class TodoController {
    private final TodoRepository repository;

    @Autowired
    public TodoController(final TodoRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET, produces = "application/json")
    public Callable<AllTodos> allTodos(@RequestHeader("user-id") final UUID userId) {
        log.info("GET all todo's for user {}", userId);
        return () -> new AllTodos(repository.get(userId));
    }

    @RequestMapping(value = "/me/{todo}", method = RequestMethod.GET, produces = "application/json")
    public Callable<ResponseEntity<TodoList>> todoList(@RequestHeader("user-id") final UUID userId, @PathVariable("todo") final String todoList) {
        log.info("GET todo {} for user {}", todoList, userId);
        return () -> repository
                .get(userId, todoList)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/me", method = RequestMethod.DELETE)
    public Callable<ResponseEntity<?>> deleteAll(@RequestHeader("user-id") final UUID userId) {
        log.info("DELETE all todo's for user {}", userId);
        return () -> {
            repository.delete(userId);

            return ResponseEntity.accepted().build();
        };
    }

    @RequestMapping(value = "/me/{todo}", method = RequestMethod.DELETE)
    public Callable<ResponseEntity<?>> deleteTodo(@RequestHeader("user-id") final UUID userId, @PathVariable("todo") final String todoList) {
        log.info("DELETE todo {} for user {}", todoList, userId);
        return () -> {
            repository.delete(userId, todoList);

            return ResponseEntity.accepted().build();
        };
    }

    @RequestMapping(value = "/me", method = RequestMethod.POST)
    public Callable<ResponseEntity<Void>> createTodoList(@RequestHeader("user-id") final UUID userId, @RequestBody TodoList todoList) {
        return () -> {
            repository.add(userId, todoList);

            return ResponseEntity.accepted().build();
        };
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @Restrict
    public Callable<ResponseEntity<?>> deleteAllTodos() {
        log.info("DELETE all todo's");
        return () -> {
            repository.deleteAll();

            return ResponseEntity.accepted().build();
        };
    }
}
