package com.sourcery.sport.team.dto;

import com.sourcery.sport.user.dto.UserDto;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {
  private UUID id;
  private String name;
  private List<UserDto> users;
  private String leaderId;
}
