package com.todo.todoBasic.service;

import java.util.List;

import com.todo.todoBasic.constants.Category;
import com.todo.todoBasic.constants.Priority;
import com.todo.todoBasic.entity.Todo;
import com.todo.todoBasic.exceptions.TodoNotFoundException;

public interface TodoService {
   void createTodo(Todo todo);
   Todo getTodo(int id);
   void deleteTodo(int id);
   List<Todo> getAllTodos();
   List<Todo> filterTodos(Priority priority , Category category)throws TodoNotFoundException;
   void checkUpcomingTasks();
   void markAsCompleted(int id);
   void markAllAsCompleted();
   void markAsUncompleted(int id);
   void markAllAsUncompleted();
   void updateTodo(int id, Todo todo);
   void clearAllTodos();
}
