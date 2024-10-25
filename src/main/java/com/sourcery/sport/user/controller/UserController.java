package com.sourcery.sport.user.controller;

import com.sourcery.sport.tournament.model.City;
import com.sourcery.sport.tournament.service.CityService;
import com.sourcery.sport.user.dto.FavoriteTournamentDto;
import com.sourcery.sport.user.dto.UserDto;
import com.sourcery.sport.user.dto.UserExistsDto;
import com.sourcery.sport.user.dto.UserIdDto;
import com.sourcery.sport.user.dto.UserStatisticsDto;
import com.sourcery.sport.user.exception.UserNotFoundException;
import com.sourcery.sport.user.model.User;
import com.sourcery.sport.user.service.UserService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  public ResponseEntity<UserDto> getUserProfile(@PathVariable String userId) {
    try {
      User user = userService.getUserProfile(userId);
      return ResponseEntity.ok(userService.toUserProfile(user));
    } catch (Exception ex) {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/profile/delete/{userId}")
  public ResponseEntity<User> deleteUser(@PathVariable String userId) {
    try {
      User user = userService.getUserById(userId);
      userService.deleteUserById(userId);
      return ResponseEntity.ok(user);
    } catch (UserNotFoundException ex) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping(value = "/check-user")
  public ResponseEntity<UserExistsDto> checkUser(@RequestBody UserIdDto userIdRequest) {
    String userId = userIdRequest.getUserId();
    boolean userExists = userService.existsUserById(userId);
    return ResponseEntity.ok(new UserExistsDto(userExists));
  }

  @PostMapping(value = "/create-user")
  public ResponseEntity<String> createUser(@RequestBody UserDto userInfo) {
    City userCity = null;
    try
    {
      userCity = cityService.getCity(userInfo.getCityId());
    } catch (Exception e) {
      ResponseEntity.internalServerError().build();
    }
    User newUser = new User(userInfo,userCity);
    userService.addNewUser(newUser);
    return ResponseEntity.ok("User information processed successfully");
  }

  @GetMapping("/get-user/{userId}")
  public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
    try {
      User user = userService.getUserById(userId);
      UserDto userDto = userService.toUserProfile(user);
      return ResponseEntity.ok(userDto);
    } catch (UserNotFoundException ex) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/update-user/{userId}")
  public ResponseEntity<UserDto> updateUser(@PathVariable String userId, @RequestBody UserDto userInfo) {
    try {
      City city = userInfo.getCityId() == null ? null : cityService.getCity(userInfo.getCityId());
      User user = userService.getUserById(userId);
      user.updateUser(userInfo,city);
      userService.addNewUser(user);
      UserDto userDto = userService.toUserProfile(user);
      return ResponseEntity.ok(userDto);
    } catch (UserNotFoundException ex) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{id}/statistics")
  public ResponseEntity<UserStatisticsDto> getUserStatistics(@PathVariable String id) {
    Integer participated = userService.getParticipatedTournamentsCount(id);
    Integer participating = userService.getParticipatingTournamentsCount(id);
    Integer won = userService.getWonTournamentsCount(id);
    UserStatisticsDto userStatisticsDto = new UserStatisticsDto(participated,participating,won);
    return ResponseEntity.ok(userStatisticsDto);
  }

  @GetMapping("/{id}/favorite-tournament")
  public ResponseEntity<FavoriteTournamentDto> getFavoriteTournament(@PathVariable String id) {
    try {
      UUID favoriteTournamentId = userService.getFavoriteTournamentType(id);
      FavoriteTournamentDto favoriteTournamentDto = new FavoriteTournamentDto(favoriteTournamentId);
      return ResponseEntity.ok(favoriteTournamentDto);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/get-all-users")
  public ResponseEntity<List<User>> getAllUsers() {
    try {
      List<User> users = userService.getAllUsers();
      return ResponseEntity.ok(users);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/search-users")
  public ResponseEntity<List<User>> searchUsers(@RequestParam String searchTerm) {
    try {
      List<User> users = userService.getUsersBySearchTerm(searchTerm);
      return ResponseEntity.ok(users);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().build();
    }
  }
}
