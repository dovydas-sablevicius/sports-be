package com.sourcery.sport.user.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class FavoriteTournamentDto {

  private final UUID tournamentUUID;

  public FavoriteTournamentDto(UUID tournamentUUID) {
    this.tournamentUUID = tournamentUUID;
  }
}
