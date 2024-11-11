package com.sourcery.sport.user.dto;

import java.util.UUID;
import com.sourcery.sport.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private String id;

  private String name;

  private String surname;

  private String email;

  private String image;

  private String phoneNumber;

  private UUID cityId;

  public UserDto(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.surname = user.getSurname();
    this.email = user.getEmail();
    this.image = user.getImage();
    this.phoneNumber = user.getPhoneNumber();
    this.cityId = user.getCity() != null ? user.getCity().getId() : null;
  }

}
