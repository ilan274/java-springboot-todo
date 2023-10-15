package com.ilanherbach.javaspringboottodo.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.security.auth.message.callback.PrivateKeyCallback.IssuerSerialNumRequest;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  // handles dependency injection
  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping()
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    // validate date
    LocalDateTime taskStartDate = taskModel.getStartAt();
    LocalDateTime taskEndDate = taskModel.getEndAt();

    LocalDateTime currentDate = LocalDateTime.now();
    if (currentDate.isAfter(taskStartDate)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          "The start date must be later than the current date.");
    }

    if (currentDate.isAfter(taskEndDate)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          "The end date must be greater than today's date.");
    }

    if (taskStartDate.isAfter(taskEndDate)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          "The start date must be greater than the end date.");
    }

    // get userId and create a task
    Object IdUser = request.getAttribute("idUser");
    taskModel.setIdUser((UUID) IdUser);
    var task = this.taskRepository.save(taskModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(task);
  }
}
