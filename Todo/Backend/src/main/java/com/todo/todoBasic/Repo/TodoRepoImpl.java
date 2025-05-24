package com.todo.todoBasic.Repo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.todoBasic.entity.Todo;
import com.todo.todoBasic.exceptions.InvalidTodoException;
import com.todo.todoBasic.exceptions.TodoNotFoundException;

@Component
public class TodoRepoImpl implements TodoRepo{

  private static final String FILE_PATH = "src/main/resources/todo.json";
  private ObjectMapper objectMapper = new ObjectMapper();

 @Override
public void saveTodo(Todo todo) throws InvalidTodoException {
    List<Todo> todos = getAllTodos();
    todo.setCreatedAt(new Date());

    int newId = todos.stream()
                     .mapToInt(Todo::getId)
                     .max()
                     .orElse(0) + 1;

    todo.setId(newId);

    if (todo.getTitle() == null || todo.getTitle().trim().isEmpty()) {
        throw new InvalidTodoException("Title cannot be null or empty");
    }

    todos.add(todo);
    saveAll(todos);
}


  @Override
  public void deleteTodo(int id) {
    List<Todo> todos = getAllTodos();
    boolean isRemoved = todos.removeIf(t->t.getId() == id);
    if(!isRemoved){
      throw new TodoNotFoundException("Todo with ID " + id + "not found");
    }
    saveAll(todos);
  }

  @Override
  public Todo getTodo(int id) {
    return getAllTodos().stream()
      .filter(t-> t.getId() == id )
      .findFirst()
      .orElseThrow(() -> new TodoNotFoundException("Todo with ID " + id + " not found."));
  }

  @Override
  public List<Todo> getAllTodos() {
   File file = new File(FILE_PATH);

   if(!file.exists()){
    return new ArrayList<>();}
    try{
      return objectMapper.readValue(file, new TypeReference<List<Todo>>(){});
      }catch(IOException e){
        e.printStackTrace();
        return new ArrayList<>();
      }
    }
  

  private void saveAll(List<Todo> todos){
    try{
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), todos);
    }catch(IOException e){
      e.printStackTrace();
    }
  }

  @Override
  public void updateTodo(Todo updatedTodo) {
    List<Todo> todos = getAllTodos();
    boolean updated = false;

    for (int i = 0; i < todos.size(); i++) {
        if (todos.get(i).getId() == updatedTodo.getId()) {
            todos.set(i, updatedTodo); 
            updated = true;
            break;
        }
    }

    if (!updated) {
        throw new TodoNotFoundException("Todo with ID " + updatedTodo.getId() + " not found");
    }

    saveAll(todos);
}

  @Override
  public void deleteAllTodos(){
    List<Todo> todos = new ArrayList<>();
    saveAll(todos);
  }
}


