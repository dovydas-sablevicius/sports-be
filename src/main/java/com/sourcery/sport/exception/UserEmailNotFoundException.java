package com.sourcery.sport.exception;

public class UserEmailNotFoundException extends RuntimeException {
  public UserEmailNotFoundException(String email) {
    super("User with email " + email + " does not exist");
  }
}
