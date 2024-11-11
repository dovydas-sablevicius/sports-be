package com.sourcery.sport.utils;

import com.sourcery.sport.user.model.User;

public final class UserFactory {

  private UserFactory() {
  }

  public static User CreateMockUser() {
    return new User(
        "123",
        "OriginalName",
        "OriginalSurname",
        "original@example.com",
        "1234567890",
        null,
        null,
        null
    );
  }
}
