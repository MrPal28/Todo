package com.todo.todoBasic.service.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.todoBasic.entity.Todo;
import com.todo.todoBasic.exceptions.TodoNotFoundException;
import com.todo.todoBasic.service.ScheduledTask;
import com.todo.todoBasic.Repo.TodoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTasksImpl implements ScheduledTask {

    @Autowired
    private TodoRepo todoRepo;

    @Override
    @Scheduled(fixedRate = 3600000)
    public void cleanUpExpiredTodos() {
        ObjectMapper mapper = new ObjectMapper();
        File expiredFile = new File("expiredtask.json");

        try {
            // Step 1: Get all todos
            List<Todo> allTodos = todoRepo.getAllTodos();
            Date now = new Date();

            // Step 2: Filter expired todos
            List<Todo> expiredTodos = allTodos.stream()
                    .filter(todo -> todo.getExpiryDate() != null && todo.getExpiryDate().before(now))
                    .collect(Collectors.toList());

            if (expiredTodos.isEmpty()) {
                System.out.println("No expired todos found.");
                return;
            }

            // Step 3: Load existing expired tasks
            List<Todo> existingExpired = new ArrayList<>();
            if (expiredFile.exists()) {
                Todo[] existing = mapper.readValue(expiredFile, Todo[].class);
                existingExpired.addAll(Arrays.asList(existing));
            }

            // Step 4: Append new expired tasks and write
            existingExpired.addAll(expiredTodos);
            mapper.writeValue(expiredFile, existingExpired);

            // Step 5: Delete expired todos from repo
            for (Todo todo : expiredTodos) {
                try {
                    todoRepo.deleteTodo(todo.getId());
                } catch (TodoNotFoundException e) {
                    // Just log and continue
                    System.err.println("Could not delete todo ID " + todo.getId() + ": " + e.getMessage());
                }
            }

            System.out.println("Moved " + expiredTodos.size() + " expired todos to expiredtask.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
