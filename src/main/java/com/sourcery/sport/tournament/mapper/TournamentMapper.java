package com.sourcery.sport.tournament.mapper;

import com.sourcery.sport.tournament.dto.TournamentCreatedDto;
import com.sourcery.sport.tournament.dto.TournamentUpdateDto;
import com.sourcery.sport.tournament.model.Tournament;

public class TournamentMapper {
  public static TournamentCreatedDto mapToTournamentCreatedDto(Tournament tournament) {
    return new TournamentCreatedDto(
        tournament.getId(),
        tournament.getName(),
        tournament.getDescription(),
        tournament.getPrizes(),
        tournament.getStartDate(),
        tournament.getEndDate(),
        tournament.getMaxParticipants(),
        tournament.getTournamentTableType().getId(),
        tournament.getTournamentType().getId(),
        tournament.getCity().getId(),
        tournament.getParticipationType(),
        tournament.getTeamSize(),
        tournament.getTournamentTagsIds()
    );
  }

  // Convert Tournament to TournamentUpdateDto
  public static TournamentUpdateDto mapToTournamentUpdateDto(Tournament updatedTournament) {
    return new TournamentUpdateDto(
        updatedTournament.getId(),
        updatedTournament.getName(),
        updatedTournament.getDescription(),
        updatedTournament.getPrizes(),
        updatedTournament.getStartDate(),
        updatedTournament.getEndDate(),
        updatedTournament.getMaxParticipants(),
        updatedTournament.getTournamentTableType().getId(),
        updatedTournament.getTournamentType().getId(),
        updatedTournament.getCity().getId(),
        updatedTournament.getParticipationType(),
        updatedTournament.getTeamSize(),
        updatedTournament.getTournamentTagsIds()
    );
  }
}
