package com.sourcery.sport.tournament.service;

import com.sourcery.sport.tournament.dto.TournamentUpdateDto;
import com.sourcery.sport.tournament.model.Tournament;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TournamentService {

  Tournament saveTournament(Tournament tournament);

  List<Tournament> getAll();

  List<Tournament> getByCityId(UUID cityId);

  Tournament getTournamentById(UUID id);

  List<Tournament> getTournamentsByDateRange(LocalDateTime startRange, LocalDateTime endRange);

  List<Tournament> getTournamentsByTags(List<UUID> tagsId);

  Tournament updateTournament(Tournament existingTournament, TournamentUpdateDto tournamentDto);
}
