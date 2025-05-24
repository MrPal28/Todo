package com.todo.todoBasic.Repo;

import java.util.List;

import com.todo.todoBasic.entity.Todo;
import com.todo.todoBasic.exceptions.InvalidTodoException;
import com.todo.todoBasic.exceptions.TodoNotFoundException;

public interface TodoRepo {

  void saveTodo(Todo todo) throws InvalidTodoException;
  void deleteTodo(int id) throws TodoNotFoundException;
  Todo getTodo(int id) throws TodoNotFoundException;
  List<Todo> getAllTodos();
  void updateTodo(Todo todo);
  void deleteAllTodos();
}