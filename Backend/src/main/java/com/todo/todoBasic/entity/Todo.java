package com.todo.todoBasic.entity;


import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.todo.todoBasic.constants.Category;
import com.todo.todoBasic.constants.Priority;
import com.todo.todoBasic.constants.Status;


@Component
public class Todo  {

  private int id ;
  private String title;
  private String description;
  private Date createdAt;
  private Date expiryDate;

  @JsonProperty("isCompleted")
  private boolean isCompleted;
  private Date updatedAt;
  private Priority priority = Priority.LOW;
  private Category category = Category.OTHER;
  private Status status = Status.ACTIVE;



  public Status getStatus() {
    return status;
  }
  public void setStatus(Status status) {
    this.status = status;
  }
  public Priority getPriority() {
    return priority;
  }
  public void setPriority(Priority priority) {
    this.priority = priority;
  }
  public Category getCategory() {
    return category;
  }
  public void setCategory(Category category) {
    this.category = category;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public Date getCreatedAt() {
    return createdAt;
  }
  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
  public Date getExpiryDate() {
    return expiryDate;
  }
  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }
  public boolean isCompleted() {
    return isCompleted;
  }
  public void setCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }
  public Date getUpdatedAt() {
    return updatedAt;
  }
  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
  
}
