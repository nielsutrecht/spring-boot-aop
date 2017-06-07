package com.nibado.example.springaop.todo.dto;

import com.nibado.example.springaop.todo.domain.TodoList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllTodos {
    private List<TodoList> todoLists;
}
