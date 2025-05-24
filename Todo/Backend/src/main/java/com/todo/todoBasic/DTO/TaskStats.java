package com.todo.todoBasic.DTO;


import java.util.Map;

import org.springframework.stereotype.Component;

import com.todo.todoBasic.constants.Category;
import com.todo.todoBasic.constants.Priority;

@Component
public class TaskStats {
  private int totalTasks;
  private int completedTasks;
  private long avgCompletionTimeInMinutes;
  private Map<Category , Integer> taskPerCategory;
  private Map<Priority,Integer> taskPerPriority;
  private int completedThisWeek;
  public int getTotalTasks() {
    return totalTasks;
  }
  public void setTotalTasks(int totalTasks) {
    this.totalTasks = totalTasks;
  }
  public int getCompletedTasks() {
    return completedTasks;
  }
  public void setCompletedTasks(int completedTasks) {
    this.completedTasks = completedTasks;
  }
  public long getAvgCompletionTimeInMinutes() {
    return avgCompletionTimeInMinutes;
  }
  public void setAvgCompletionTimeInMinutes(long avgCompletionTimeInMinutes) {
    this.avgCompletionTimeInMinutes = avgCompletionTimeInMinutes;
  }
  public Map<Category, Integer> getTaskPerCategory() {
    return taskPerCategory;
  }
  public void setTaskPerCategory(Map<Category, Integer> taskPerCategory) {
    this.taskPerCategory = taskPerCategory;
  }
  public Map<Priority, Integer> getTaskPerPriority() {
    return taskPerPriority;
  }
  public void setTaskPerPriority(Map<Priority, Integer> taskPerPriority) {
    this.taskPerPriority = taskPerPriority;
  }
  public int getCompletedThisWeek() {
    return completedThisWeek;
  }
  public void setCompletedThisWeek(int completedThisWeek) {
    this.completedThisWeek = completedThisWeek;
  }
  

}
