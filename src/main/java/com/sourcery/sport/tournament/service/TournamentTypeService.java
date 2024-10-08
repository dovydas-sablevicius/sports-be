package com.sourcery.sport.tournament.service;

import com.sourcery.sport.tournament.model.TournamentType;
import java.util.List;
import java.util.UUID;

public interface TournamentTypeService {
  TournamentType saveTournamentType(TournamentType tournamentType);

  TournamentType getTournamentType(UUID id);

  List<TournamentType> getAll();

}
