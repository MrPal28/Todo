package com.todo.todoBasic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todo.todoBasic.DTO.TaskStats;
import com.todo.todoBasic.constants.Category;
import com.todo.todoBasic.constants.Priority;
import com.todo.todoBasic.entity.Todo;
import com.todo.todoBasic.service.TaskAnalyticsService;
import com.todo.todoBasic.service.TodoService;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping(("/todos"))
public class TodoController{
  
     @Autowired
     private TodoService todoService;

     @Autowired
     private TaskAnalyticsService taskAnalyticsService; 

     @PostMapping("/addTodo")  //Endpoint to add a new Todo
     public void addTodo(@RequestBody Todo todo){
      todoService.createTodo(todo);
     }

     @GetMapping("/{id}")      //Endpoint to get a Todo by ID
     public Todo getTodo(@PathVariable int id){
          return todoService.getTodo(id);
     }

     @DeleteMapping("/{id}")         ///End point to delete a Todo by ID
     public void deleteTodo(@PathVariable int id){
          todoService.deleteTodo(id);
     }

     @GetMapping         //End point to get all Todos
     public List<Todo> getAllTodos(){
          return todoService.getAllTodos();
     }

     @GetMapping("/filter")      //End point to filter Todos by category and priority
     public List<Todo> filterByCategoryAndPriority(@RequestParam Category category, @RequestParam Priority priority){
          return todoService.filterTodos(priority, category);
     }
     //add endpoints mark as completed and uncompleted todos
     @PutMapping("/markAsCompleted/{id}")
     public void markAsCompleted(@PathVariable int id){
          todoService.markAsCompleted(id);
     }
     //end point to update a Todo
     @PutMapping("/update/{id}")
     public void updateTodo(@PathVariable int id , @RequestBody Todo todo){
          todoService.updateTodo(id,todo);
     }
     //end point to delete all todo
     @DeleteMapping("/deleteAll")
     public void deleteAllTodos(){
          todoService.clearAllTodos();
     } 

     @PutMapping("markAllAsCompleted")
     public void markAllAsCompleted(){
          todoService.markAllAsCompleted();
     }

     @PutMapping("/markAsUncompleted/{id}")
     public void markAsUncompleted(@PathVariable int id){
          todoService.markAsUncompleted(id);
     }

     @PutMapping("/markAllAsUncompleted")
     public void markAllAsUncompleted(){
          todoService.markAllAsUncompleted();
     }

     @GetMapping("/analytics")
     public TaskStats getStats() {
    return taskAnalyticsService.generateAnalytics();
}
}
