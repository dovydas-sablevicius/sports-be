package com.sourcery.sport.user.dto;

import java.util.UUID;

public class FavoriteTournamentDto {
  private final UUID tournamentUUID;

  public FavoriteTournamentDto(UUID tournamentUUID) {
    this.tournamentUUID = tournamentUUID;
  }
}
