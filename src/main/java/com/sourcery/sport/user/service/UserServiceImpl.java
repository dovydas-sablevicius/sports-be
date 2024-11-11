package com.sourcery.sport.user.service;

import com.sourcery.sport.user.dto.UserDto;
import com.sourcery.sport.user.exception.UserNotFoundException;
import com.sourcery.sport.user.model.User;
import com.sourcery.sport.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User addNewUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public UserDto toUserProfile(User user) {
    if (user.getCity() == null) {
      return new UserDto(user.getId(), user.getName(),
          user.getSurname(), user.getEmail(),
          user.getImage(), user.getPhoneNumber(), null
      );
    }
    UserDto userDto = new UserDto(user.getId(), user.getName(),
        user.getSurname(), user.getEmail(),
        user.getImage(), user.getPhoneNumber(),
        user.getCity().getId()
    );
    return userDto;
  }

  @Override
  public User getUserByEmail(String email) {
    return userRepository.findUserByEmail(email);
  }

  @Override
  public User getUserById(String id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  @Override
  public void deleteUserById(String id) {
    userRepository.deleteById(id);
  }

  @Override
  public boolean existsUserById(String id) {
    return userRepository.existsById(id);
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public List<User> getUsersBySearchTerm(String term) {
    return userRepository.findUsersBySearchTerm(term);
  }

  @Override
  public Integer getParticipatedTournamentsCount(String userId) {
    return userRepository.getParticipatedTournamentsCount(userId);
  }

  @Override
  public Integer getParticipatingTournamentsCount(String userId) {
    return userRepository.getParticipatingTournamentsCount(userId);
  }

  @Override
  public Integer getWonTournamentsCount(String userId) {
    return userRepository.getWonTournamentsCount(userId);
  }

  @Override
  public UUID getFavoriteTournamentType(String userId) {
    return userRepository.getFavoriteTournamentType(userId);
  }
}
