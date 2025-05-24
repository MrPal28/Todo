package com.todo.todoBasic.service.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.todoBasic.entity.Todo;
import com.todo.todoBasic.service.ExpiredTaskService;

@Service
public class ExpiredTaskServiceImpl implements ExpiredTaskService{

    private final String EXPIRED_TASKS_FILE = "expiredtask.json";
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Todo> getAllExpiredTasks() {
        try {
            File file = new File(EXPIRED_TASKS_FILE);
            if (!file.exists()) return new ArrayList<>();
            return Arrays.asList(mapper.readValue(file, Todo[].class));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveAllExpiredTasks(List<Todo> expiredTasks) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(EXPIRED_TASKS_FILE), expiredTasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteExpiredTaskById(int id) {
        List<Todo> expiredTasks = getAllExpiredTasks();
        expiredTasks = expiredTasks.stream()
            .filter(task -> task.getId() != id)
            .collect(Collectors.toList());
        saveAllExpiredTasks(expiredTasks);
    }

    public void clearAllExpiredTasks() {
        saveAllExpiredTasks(new ArrayList<>());
    }

    
}
