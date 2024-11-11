package com.sourcery.sport.mocks;

import com.sourcery.sport.tournament.model.TournamentType;
import com.sourcery.sport.tournament.service.TournamentTypeService;
import java.util.*;

public class TournamentTypeServiceMock implements TournamentTypeService {
  private final Map<UUID, TournamentType> typeStore = new HashMap<>();

  @Override
  public TournamentType saveTournamentType(TournamentType tournamentType) {
    typeStore.put(tournamentType.getId(), tournamentType);
    return tournamentType;
  }

  @Override
  public TournamentType getTournamentType(UUID id) {
    return typeStore.get(id);
  }

  @Override
  public List<TournamentType> getAll() {
    return new ArrayList<>(typeStore.values());
  }
}

