package com.sourcery.sport.tournamenttag.service;

import com.sourcery.sport.tournamenttag.model.TournamentTag;
import java.util.List;
import java.util.UUID;

public interface TournamentTagService {

  TournamentTag getTagById(UUID id);

  TournamentTag saveTag(TournamentTag tournamentTag);

  List<TournamentTag> getAllTags();
}
