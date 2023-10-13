package com.ilanherbach.javaspringboottodo.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @PostMapping()
  public void ListUsersController(@RequestBody UserModel userModel) {
    System.out.println("Hello, " + userModel.toString());
  }
}
