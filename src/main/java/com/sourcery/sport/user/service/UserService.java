package com.sourcery.sport.user.service;

import com.sourcery.sport.user.dto.UserDto;
import com.sourcery.sport.user.model.User;
import java.util.List;
import java.util.UUID;

public interface UserService {

  User addNewUser(User user);

  UserDto toUserProfile(User user);

  User getUserByEmail(String email);

  User getUserById(String id);

  void deleteUserById(String id);

  boolean existsUserById(String id);

  List<User> getAllUsers();

  List<User> getUsersBySearchTerm(String term);

  Integer getParticipatedTournamentsCount(String userId);

  Integer getParticipatingTournamentsCount(String userId);

  Integer getWonTournamentsCount(String userId);

  UUID getFavoriteTournamentType(String userId);
}

