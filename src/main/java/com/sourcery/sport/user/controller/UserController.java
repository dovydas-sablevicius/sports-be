package com.sourcery.sport.user.controller;

import com.sourcery.sport.tournament.service.CityService;
import com.sourcery.sport.user.dto.UserDto;
import com.sourcery.sport.user.dto.UserIdDto;
import com.sourcery.sport.user.exception.UserNotFoundException;
import com.sourcery.sport.user.model.User;
import com.sourcery.sport.user.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.ResponseBody;
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
  @ResponseBody
  public ResponseEntity<?> getUserProfile(@PathVariable String userId) {
    try {
      User user = userService.getUserProfile(userId);
      return ResponseEntity.ok(userService.toUserProfile(user));
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @DeleteMapping("/profile/delete/{userId}")
  @ResponseBody
  public ResponseEntity<?> deleteUser(@PathVariable String userId) {
    try {
      User user = userService.getUserById(userId);
      userService.deleteUserById(userId);
      return ResponseEntity.ok(user);
    } catch (UserNotFoundException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PostMapping(value = "/check-user")
  public ResponseEntity<?> checkUser(@RequestBody UserIdDto userIdRequest) {
    String userId = userIdRequest.getUserId();
    boolean userExists = userService.existsUserById(userId);

    if (userExists) {
      return ResponseEntity.ok(Map.of("userExists", true));
    } else {
      return ResponseEntity.ok(Map.of("userExists", false));
    }
  }

  @PostMapping(value = "/create-user")
  public ResponseEntity<?> createUser(@RequestBody UserDto userInfo) {
    User newUser = new User();
    newUser.setId(userInfo.getId());
    newUser.setEmail(userInfo.getEmail());
    newUser.setName(userInfo.getName());
    newUser.setSurname(userInfo.getSurname());
    newUser.setImage(userInfo.getImage());
    newUser.setPhoneNumber(userInfo.getPhoneNumber());
    newUser.setCity(cityService.getCity(userInfo.getCityId()));
    userService.addNewUser(newUser);

    return ResponseEntity.ok("User information processed successfully");
  }

  @GetMapping("/get-user/{userId}")
  @ResponseBody
  public ResponseEntity<?> getUserById(@PathVariable String userId) {
    try {
      User user = userService.getUserById(userId);
      UserDto userDto = userService.toUserProfile(user);
      return ResponseEntity.ok(userDto);
    } catch (UserNotFoundException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PutMapping("/update-user/{userId}")
  @ResponseBody
  public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDto userInfo) {
    try {
      User user = userService.getUserById(userId);
      user.setName(userInfo.getName());
      user.setSurname(userInfo.getSurname());
      user.setEmail(userInfo.getEmail());
      user.setImage(userInfo.getImage());
      user.setPhoneNumber(userInfo.getPhoneNumber());

      if (userInfo.getCityId() != null) {
        user.setCity(cityService.getCity(userInfo.getCityId()));
      } else {
        user.setCity(null);
      }

      userService.addNewUser(user);

      UserDto userDto = userService.toUserProfile(user);
      return ResponseEntity.ok(userDto);
    } catch (UserNotFoundException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/{id}/statistics")
  @ResponseBody
  public ResponseEntity<Map<String, Integer>> getUserStatistics(@PathVariable String id) {
    Map<String, Integer> statistics = new HashMap<>();

    Integer participated = userService.getParticipatedTournamentsCount(id);
    Integer participating = userService.getParticipatingTournamentsCount(id);
    Integer won = userService.getWonTournamentsCount(id);

    statistics.put("participated", participated != null ? participated : 0);
    statistics.put("participating", participating != null ? participating : 0);
    statistics.put("won", won != null ? won : 0);

    return ResponseEntity.ok(statistics);
  }

  @GetMapping("/{id}/favorite-tournament")
  @ResponseBody
  public ResponseEntity<?> getFavoriteTournament(@PathVariable String id) {
    try {
      UUID favoriteTournamentId = userService.getFavoriteTournamentType(id);
      return ResponseEntity.ok(favoriteTournamentId);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/get-all-users")
  @ResponseBody
  public ResponseEntity<?> getAllUsers() {
    try {
      List<User> users = userService.getAllUsers();
      return ResponseEntity.ok(users);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/search-users")
  @ResponseBody
  public ResponseEntity<?> searchUsers(@RequestParam String searchTerm) {
    try {
      List<User> users = userService.getUsersBySearchTerm(searchTerm);
      return ResponseEntity.ok(users);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
