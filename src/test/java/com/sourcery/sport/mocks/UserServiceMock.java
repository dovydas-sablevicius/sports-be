package com.sourcery.sport.mocks;

import com.sourcery.sport.user.dto.UserDto;
import com.sourcery.sport.user.exception.UserNotFoundException;
import com.sourcery.sport.user.model.User;
import com.sourcery.sport.user.service.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserServiceMock implements UserService {

  private final Map<String, User> userStore = new HashMap<>();
  private final Map<String, Integer> userTournamentParticipationCount = new HashMap<>();
  private final Map<String, Integer> userTournamentParticipatingCount = new HashMap<>();
  private final Map<String, Integer> userTournamentWonCount = new HashMap<>();
  private final Map<String, UUID> userFavoriteTournament = new HashMap<>();

  @Override
  public User addNewUser(User user) {
    userStore.put(user.getId(), user);
    return user;
  }

  @Override
  public UserDto toUserProfile(User user) {
    return new UserDto(user);
  }

  @Override
  public User getUserByEmail(String email) {
    return userStore.values().stream()
        .filter(user -> email.equals(user.getEmail()))
        .findFirst()
        .orElse(null);
  }

  @Override
  public User getUserById(String id) {
    User user = userStore.get(id);
    if (user == null) {
      throw new UserNotFoundException(id);
    }
    return user;
  }

  @Override
  public void deleteUserById(String id) {
    User removedUser = userStore.remove(id);
    if (removedUser == null) {
      throw new UserNotFoundException(id);
    }
  }

  @Override
  public boolean existsUserById(String id) {
    return userStore.containsKey(id);
  }

  @Override
  public List<User> getAllUsers() {
    return new ArrayList<>(userStore.values());
  }

  @Override
  public List<User> getUsersBySearchTerm(String term) {
    return userStore.values().stream()
        .filter(user -> user.getName().contains(term)).toList();
  }

  @Override
  public Integer getParticipatedTournamentsCount(String userId) {
    return userTournamentParticipationCount.getOrDefault(userId, 0);
  }

  @Override
  public Integer getParticipatingTournamentsCount(String userId) {
    return userTournamentParticipatingCount.getOrDefault(userId, 0);
  }

  @Override
  public Integer getWonTournamentsCount(String userId) {
    return userTournamentWonCount.getOrDefault(userId, 0);
  }

  @Override
  public UUID getFavoriteTournamentType(String userId) {
    return userFavoriteTournament.getOrDefault(userId, new UUID(0, 0));
  }

  public void updateParticipationCount(String userId, int count) {
    userTournamentParticipationCount.put(userId, count);
  }

  public void updateParticipatingCount(String userId, int count) {
    userTournamentParticipatingCount.put(userId, count);
  }

  public void updateWonCount(String userId, int count) {
    userTournamentWonCount.put(userId, count);
  }

  public void updateUserFavoriteTournament(String userId, UUID tournamentId) {
    userFavoriteTournament.put(userId, tournamentId);
  }
}
