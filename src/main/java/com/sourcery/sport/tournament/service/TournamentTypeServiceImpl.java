package com.sourcery.sport.tournament.service;

import com.sourcery.sport.tournament.model.TournamentType;
import com.sourcery.sport.tournament.repository.TournamentTypeRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TournamentTypeServiceImpl implements TournamentTypeService {

  final TournamentTypeRepository tournamentTypeRepository;

  public TournamentTypeServiceImpl(TournamentTypeRepository tournamentTypeRepository) {
    this.tournamentTypeRepository = tournamentTypeRepository;
  }

  @Override
  public TournamentType saveTournamentType(TournamentType tournamentType) {
    return tournamentTypeRepository.save(tournamentType);
  }

  @Override
  public TournamentType getTournamentType(UUID id) {
    return  tournamentTypeRepository.getReferenceById(id);
  }

  @Override
  public List<TournamentType> getAll() {
    return tournamentTypeRepository.findAll();
  }
}
