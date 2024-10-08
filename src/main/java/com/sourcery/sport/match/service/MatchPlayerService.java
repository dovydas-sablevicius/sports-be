package com.sourcery.sport.match.service;

import com.sourcery.sport.match.dto.MatchPlayerDto;
import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.match.model.MatchPlayer;
import java.util.List;

public interface MatchPlayerService {
  void saveMatchPlayers(List<MatchPlayer> matchPlayers);

  List<MatchPlayer> mapMatchPlayers(List<MatchPlayerDto> matchPlayerDtos, Match match);

  MatchPlayer addMatchPlayer(MatchPlayerDto matchPlayerDto, Match match);

  void updateMatchPlayers(List<MatchPlayer> matchPlayer, Match match);

  boolean isMatchPlayerInMatch(MatchPlayer matchPlayer, Match match);
}
