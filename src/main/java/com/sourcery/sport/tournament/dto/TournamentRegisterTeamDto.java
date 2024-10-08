package com.sourcery.sport.tournament.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TournamentRegisterTeamDto {

  private String teamName;

  private List<String> emails;

  private String leaderId;

}
