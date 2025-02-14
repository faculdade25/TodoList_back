package com.dev.todo3.controller;

import com.dev.todo3.entity.Task;
import com.dev.todo3.entity.TodoList;
import com.dev.todo3.service.TodoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/lists")
public class TodoListController {

    private final TodoListService todoListService;

    @Autowired
    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<TodoList> createTodoList(
            @PathVariable Long userId,
            @RequestBody TodoList todoList
    ) {
        TodoList createdList = todoListService.createTodoList(userId, todoList);
        return ResponseEntity.ok(createdList);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TodoList>> getAllTodoListsByUser(@PathVariable Long userId) {
        List<TodoList> lists = todoListService.getAllTodoListsByUser(userId);
        return ResponseEntity.ok(lists);
    }


    @GetMapping("/{listId}")
    public ResponseEntity<TodoList> getTodoListById(@PathVariable Long listId) {
        Optional<TodoList> todoList = todoListService.getTodoListById(listId);
        return todoList.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/task/add/{listId}")
    public ResponseEntity<TodoList> addTaskToList(@PathVariable Long listId, @RequestBody Task task){
        TodoList todoList = todoListService.addTaskToList(listId, task);
        return ResponseEntity.ok(todoList);
    }

    @PutMapping("/{listId}/tasks/{taskId}")
    public ResponseEntity<TodoList> updateTaskInList(@PathVariable Long listId, @PathVariable Long taskId, @RequestBody Task updatedTask) {
        TodoList updatedList = todoListService.updateTaskInList(listId, taskId, updatedTask);
        return ResponseEntity.ok(updatedList);
    }

    @DeleteMapping("/{listId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTaskFromList( @PathVariable Long listId, @PathVariable Long taskId) {
        todoListService.deleteTaskFromList(listId, taskId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{listId}")
    public ResponseEntity<TodoList> updateTodoList(
            @PathVariable Long listId,
            @RequestBody TodoList updatedList
    ) {
        TodoList updated = todoListService.updateTodoList(listId, updatedList);
        return ResponseEntity.ok(updated);
    }

    // Deleta uma lista pelo ID
    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteTodoList(@PathVariable Long listId) {
        todoListService.deleteTodoList(listId);
        return ResponseEntity.noContent().build();
    }
}