package com.todo.todoBasic.service.serviceImpl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todo.todoBasic.DTO.TaskStats;
import com.todo.todoBasic.Repo.TodoRepo;
import com.todo.todoBasic.constants.Category;
import com.todo.todoBasic.constants.Priority;
import com.todo.todoBasic.entity.Todo;
import com.todo.todoBasic.service.TaskAnalyticsService;

@Service
public class TaskAnalyticsServiceImpl implements TaskAnalyticsService {

  @Autowired
  private TodoRepo todoRepo;

  @Override
  public TaskStats generateAnalytics() {
    List<Todo> todos = todoRepo.getAllTodos();

    TaskStats taskStats = new TaskStats();
    int total = todos.size();
    int completed = (int) todos.stream()
        .filter(Todo::isCompleted)
        .count();

    LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
    // Completed This Week
  
    int completedThisWeek = (int) todos.stream()
        .filter(todo -> todo.isCompleted()
            && todo.getUpdatedAt() != null && todo.getUpdatedAt()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .isAfter(oneWeekAgo))
        .count();
    // Avg completion time in minutes
    List<Long> completionTimes = todos.stream()
        .filter(todo -> todo.isCompleted() &&
            todo.getCreatedAt() != null && todo.getUpdatedAt() != null)
        .map(todo -> {
          Instant created = todo.getCreatedAt().toInstant();
          Instant completedAt = todo.getUpdatedAt().toInstant();
          return Duration.between(created, completedAt).toMinutes();
        })
        .collect(Collectors.toList());
    long avgCompletionTime = completionTimes.isEmpty() ? 0
        : completionTimes.stream().mapToLong(Long::longValue).sum() / completionTimes.size();

    // Tasks per category
    Map<Category, Integer> taskPerCategory = todos.stream().collect(
        Collectors.groupingBy(Todo::getCategory, Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));

    // Tasks per Pririty
    Map<Priority, Integer> tasksPerPriority = todos.stream()
        .collect(Collectors.groupingBy(Todo::getPriority,
            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));

    // set values
    taskStats.setTotalTasks(total);
    taskStats.setCompletedTasks(completed);
    taskStats.setCompletedThisWeek(completedThisWeek);
    taskStats.setAvgCompletionTimeInMinutes(avgCompletionTime);
    taskStats.setTaskPerCategory(taskPerCategory);
    taskStats.setTaskPerPriority(tasksPerPriority);

    return taskStats;
  }

}
