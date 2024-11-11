package com.sourcery.sport.user.controller;

import com.sourcery.sport.user.model.User;
import com.sourcery.sport.user.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/search")
public class UserSearchController {

  private final UserService userService;

  public UserSearchController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/getAll")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/searchUsers")
  public ResponseEntity<List<User>> searchUsers(@RequestParam String searchTerm) {
    List<User> users = userService.getUsersBySearchTerm(searchTerm);
    return ResponseEntity.ok(users);
  }
}
