package com.sourcery.sport.tournamenttag.service;

import com.sourcery.sport.tournamenttag.model.TournamentTag;
import com.sourcery.sport.tournamenttag.repository.TournamentTagRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TournamentTagServiceImpl implements TournamentTagService {

  final TournamentTagRepository tournamentTagRepository;

  public TournamentTagServiceImpl(TournamentTagRepository tournamentTagRepository) {
    this.tournamentTagRepository = tournamentTagRepository;
  }

  @Override
  public TournamentTag getTagById(UUID id) {
    return tournamentTagRepository.findTournamentTagById(id);
  }

  @Override
  public TournamentTag saveTag(TournamentTag tournamentTag) {
    return tournamentTagRepository.save(tournamentTag);
  }

  @Override
  public List<TournamentTag> getAllTags() {
    return tournamentTagRepository.findAll();
  }
}
