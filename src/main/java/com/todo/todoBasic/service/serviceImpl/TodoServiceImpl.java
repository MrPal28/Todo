package com.todo.todoBasic.service.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.todo.todoBasic.Repo.TodoRepo;
import com.todo.todoBasic.constants.Category;
import com.todo.todoBasic.constants.Priority;
import com.todo.todoBasic.entity.Todo;
import com.todo.todoBasic.exceptions.TodoNotFoundException;
import com.todo.todoBasic.service.TodoService;

@Service
public class TodoServiceImpl implements TodoService {

  @Autowired
  private TodoRepo todoRepo;

  @Override
  public void createTodo(Todo todo) {
     todoRepo.saveTodo(todo);
  }

  @Override
  public Todo getTodo(int id) {
    return todoRepo.getTodo(id);
  }

  @Override
  public void deleteTodo(int id) {
    todoRepo.deleteTodo(id);
  }

  @Override
  public List<Todo> getAllTodos() {
  return todoRepo.getAllTodos();
  }

 @Override
public List<Todo> filterTodos(Priority priority, Category category) throws TodoNotFoundException {
    List<Todo> todos = todoRepo.getAllTodos().stream()
            .filter(t -> (category == null || t.getCategory() == category))
            .filter(t -> (priority == null || t.getPriority() == priority))
            .collect(Collectors.toList());

    if (todos.isEmpty()) {
        throw new TodoNotFoundException("No Todos found for the given filters");
    }

    return todos;
}


  @Override
  @Scheduled(fixedRate = 7200000) ///2 Hrs.
  public void checkUpcomingTasks(){
        List<Todo> allTodos = todoRepo.getAllTodos();
    Date now = new Date();
    
    for(Todo todo: allTodos){
      long timeLeft = todo.getExpiryDate().getTime() - now.getTime();

      if(timeLeft > 0 && timeLeft <= 864000000 && !todo.isCompleted()){
        System.out.println("Remainder: Task "+todo.getTitle()+ " is due soon!");
      }
    }

  }

  @Override
  public void markAsCompleted(int id) {
   Todo todo = todoRepo.getTodo(id);
   if(todo != null){
    todo.setCompleted(true);
    todo.setUpdatedAt(new Date());
    todoRepo.updateTodo(todo);
   }else{
    throw new TodoNotFoundException("Todo with ID " + id + "not found");
   }
  }
  
  @Override
  public void markAllAsCompleted(){
    List<Todo> allTodos = todoRepo.getAllTodos();
    for(Todo todo: allTodos){
      todo.setCompleted(true);
      todo.setUpdatedAt(new Date());
      todoRepo.updateTodo(todo);
    }
  }

  @Override
  public void markAsUncompleted(int id){
    Todo todo = todoRepo.getTodo(id);
    if(todo != null){
      todo.setCompleted(false);
      todoRepo.updateTodo(todo);
    }else{
      throw new TodoNotFoundException("Todo with ID "+ id + " not found");
    }
  }

  @Override
  public void markAllAsUncompleted(){
    List<Todo> allTodos = todoRepo.getAllTodos();
    for(Todo todo: allTodos){
      todo.setCompleted(false);
      todoRepo.updateTodo(todo);
    }
  }

  @Override
  public void updateTodo(int id, Todo todo){
    Todo existingTodo = todoRepo.getTodo(id);
    if(existingTodo!=null){
      existingTodo.setTitle(todo.getTitle()!=null? todo.getTitle():existingTodo.getTitle());
      existingTodo.setDescription(null!=todo.getDescription()? todo.getDescription():existingTodo.getDescription());
      existingTodo.setCategory(null!=todo.getCategory()? todo.getCategory():existingTodo.getCategory());
      existingTodo.setPriority(null!=todo.getPriority()?todo.getPriority():existingTodo.getPriority());
      existingTodo.setExpiryDate(null!=todo.getExpiryDate()?todo.getExpiryDate():existingTodo.getExpiryDate());
      existingTodo.setCompleted(todo.isCompleted());
      existingTodo.setUpdatedAt(new Date());
      todoRepo.updateTodo(existingTodo);
    }
    else{
      throw new TodoNotFoundException("Todo with ID "+ id +" not found");
    }
  }

  @Override
  public void clearAllTodos() {
    todoRepo.deleteAllTodos();
  }

}
