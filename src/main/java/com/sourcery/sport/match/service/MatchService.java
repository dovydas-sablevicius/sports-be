package com.sourcery.sport.match.service;

import com.sourcery.sport.match.dto.MatchDto;
import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.tournament.model.Tournament;
import java.util.List;

public interface MatchService {
  void saveMatch(Match match);

  Match mapMatch(MatchDto matchDto, Tournament tournament);

  List<Match> getMatchesByTournament(Tournament tournament);

  Match getMatchById(String id);

  void updateMatches(List<Match> match);
}
