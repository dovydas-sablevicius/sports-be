package com.sourcery.sport.tournament.service;

import com.sourcery.sport.tournament.model.TournamentTableType;
import java.util.List;
import java.util.UUID;

public interface TournamentTableTypeService {
  TournamentTableType saveTournamentTableType(TournamentTableType tournamentTableType);

  TournamentTableType getTournamentTableType(UUID id);

  List<TournamentTableType> getAll();
}
