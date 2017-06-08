package com.nibado.example.springaop.todo;

import com.nibado.example.springaop.aspects.Timed;
import com.nibado.example.springaop.todo.domain.TodoList;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.lang.String.format;

@Repository
public class TodoRepository {
    private Map<UUID, List<TodoList>> db = new HashMap<>();

    @Timed
    public void add(final UUID userId, final TodoList list) {
        if (get(userId, list.getName()).isPresent()) {
            throw new IllegalArgumentException(format("List with name %s already exists", list.getName()));
        }
        if (list.getTodos() == null) {
            list.setTodos(new ArrayList<>());
        }
        get(userId).add(list);
    }

    @Timed
    public List<TodoList> get(final UUID userId) {
        return db.computeIfAbsent(userId, u -> new ArrayList<>());
    }

    @Timed
    public Optional<TodoList> get(final UUID userId, final String todoList) {
        return get(userId).stream().filter(l -> l.getName().equalsIgnoreCase(todoList)).findAny();
    }

    @Timed
    public void delete(final UUID userId) {
        db.remove(userId);
    }

    @Timed
    public void delete(final UUID userId, final String todoList) {
        List<TodoList> list = get(userId);

        list.removeIf(l -> l.getName().equals(todoList));
    }

    @Timed
    public void deleteAll() {
        db.clear();
    }
}
