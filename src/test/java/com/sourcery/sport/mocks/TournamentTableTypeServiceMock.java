package com.sourcery.sport.mocks;

import com.sourcery.sport.tournament.model.TournamentTableType;
import com.sourcery.sport.tournament.service.TournamentTableTypeService;
import java.util.*;

public class TournamentTableTypeServiceMock implements TournamentTableTypeService {
  private final Map<UUID, TournamentTableType> tableTypeStore = new HashMap<>();

  @Override
  public TournamentTableType saveTournamentTableType(TournamentTableType tournamentTableType) {
    tableTypeStore.put(tournamentTableType.getId(), tournamentTableType);
    return tournamentTableType;
  }

  @Override
  public TournamentTableType getTournamentTableType(UUID id) {
    return tableTypeStore.get(id);
  }

  @Override
  public List<TournamentTableType> getAll() {
    return new ArrayList<>(tableTypeStore.values());
  }
}
