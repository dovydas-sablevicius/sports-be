package com.sourcery.sport.user.controller;

import com.sourcery.sport.global.GenericResponse;
import com.sourcery.sport.tournament.model.City;
import com.sourcery.sport.tournament.service.CityService;
import com.sourcery.sport.user.dto.UserDto;
import com.sourcery.sport.user.dto.UserExistsDto;
import com.sourcery.sport.user.model.User;
import com.sourcery.sport.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final CityService cityService;

  public UserController(UserService userService, CityService cityService) {
    this.userService = userService;
    this.cityService = cityService;
  }

  @GetMapping("/profile/{userId}")
  public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
    User user = userService.getUserById(userId);
    return ResponseEntity.ok(userService.toUserProfile(user));

  }

  @GetMapping(value = "/check/{userId}")
  public ResponseEntity<UserExistsDto> checkUser(@PathVariable String userId) {
    boolean userExists = userService.existsUserById(userId);
    return ResponseEntity.ok(new UserExistsDto(userExists));
  }

  @DeleteMapping("{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
    userService.deleteUserById(userId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping(value = "/create")
  public ResponseEntity<GenericResponse> createUser(@RequestBody UserDto userInfo) {
    City userCity;
    userCity = cityService.getCity(userInfo.getCityId());
    User newUser = new User(userInfo, userCity);
    userService.addNewUser(newUser);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/update-user/{userId}")
  public ResponseEntity<UserDto> updateUser(@PathVariable String userId, @RequestBody UserDto userInfo) {
    City city = userInfo.getCityId() == null ? null : cityService.getCity(userInfo.getCityId());
    User user = userService.getUserById(userId);
    user.updateUser(userInfo, city);
    userService.addNewUser(user);
    UserDto userDto = userService.toUserProfile(user);
    return ResponseEntity.ok(userDto);
  }

}
