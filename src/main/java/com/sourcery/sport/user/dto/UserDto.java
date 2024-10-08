package com.sourcery.sport.user.dto;

import java.util.UUID;
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

}
