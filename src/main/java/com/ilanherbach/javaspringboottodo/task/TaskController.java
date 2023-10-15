package com.ilanherbach.javaspringboottodo.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    Object idUser = request.getAttribute("idUser");
    taskModel.setIdUser((UUID) idUser);
    var task = this.taskRepository.save(taskModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(task);
  }

  @GetMapping()
  public ResponseEntity list(HttpServletRequest request) {
    Object idUser = request.getAttribute("idUser");
    List<TaskModel> userTasks = this.taskRepository.findByIdUser((UUID) idUser);

    return ResponseEntity.status(HttpStatus.OK).body(userTasks);
  }
}
