package com.todo.todoBasic.service;

import java.util.List;

import com.todo.todoBasic.entity.Todo;

public interface ExpiredTaskService {
  public List<Todo> getAllExpiredTasks();
  public void saveAllExpiredTasks(List<Todo> expiredTasks);
   public void deleteExpiredTaskById(int id) ;
    public void clearAllExpiredTasks();
}
