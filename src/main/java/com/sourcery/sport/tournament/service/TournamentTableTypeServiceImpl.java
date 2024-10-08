package com.sourcery.sport.tournament.service;

import com.sourcery.sport.tournament.model.TournamentTableType;
import com.sourcery.sport.tournament.repository.TournamentTableTypeRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TournamentTableTypeServiceImpl implements TournamentTableTypeService {

  final TournamentTableTypeRepository tournamentTableTypeRepository;

  public TournamentTableTypeServiceImpl(TournamentTableTypeRepository tournamentTableTypeRepository) {
    this.tournamentTableTypeRepository = tournamentTableTypeRepository;
  }

  @Override
  public TournamentTableType saveTournamentTableType(TournamentTableType tournamentTableType) {
    return tournamentTableTypeRepository.save(tournamentTableType);
  }

  @Override
  public TournamentTableType getTournamentTableType(UUID id) {
    return tournamentTableTypeRepository.getReferenceById(id);
  }

  @Override
  public List<TournamentTableType> getAll() {
    return tournamentTableTypeRepository.findAll();
  }
}
