package com.sourcery.sport.mocks;

import com.sourcery.sport.match.dto.MatchDto;
import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.match.service.MatchService;
import com.sourcery.sport.tournament.model.Tournament;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchServiceMock implements MatchService {

  // Map to simulate the Match repository (Match -> String as matchId)
  private final Map<Match, String> matchDatabase = new HashMap<>();

  @Override
  public void saveMatch(Match match) {
    // Save the match into the map with match as the key and its matchId as the value
    matchDatabase.put(match, match.getMatchId());
  }

  @Override
  public Match mapMatch(MatchDto matchDto, Tournament tournament) {
    Match match = new Match();
    match.setMatchId(matchDto.getId());
    match.setTournament(tournament);
    match.setState(matchDto.getState());
    match.setRound(Integer.parseInt(matchDto.getTournamentRoundText()));
    match.setStartTime(LocalDate.parse(matchDto.getStartTime()));
    if (matchDto.getIsUpdated() != null) {
      match.setIsUpdated(matchDto.getIsUpdated());
    } else {
      match.setIsUpdated(false);
    }
    match.setNextMatchId(matchDto.getNextMatchId());
    match.setMatchNumber(matchDto.getMatchNumber());
    return match;
  }

  @Override
  public List<Match> getMatchesByTournament(Tournament tournament) {
    // Return all matches that belong to the specified tournament by filtering the map
    return matchDatabase.keySet().stream()
        .filter(match -> match.getTournament().equals(tournament))
        .collect(Collectors.toList());
  }

  @Override
  public Match getMatchById(String id) {
    // Find match in the map by its matchId
    return matchDatabase.keySet().stream()
        .filter(match -> match.getMatchId().equals(id))
        .findFirst()
        .orElse(null);
  }

  @Override
  public void updateMatches(List<Match> updatedMatches) {
    // Update the matches in the map with the new data
    for (Match updatedMatch : updatedMatches) {
      Match existingMatch = getMatchById(updatedMatch.getMatchId());
      if (existingMatch != null) {
        existingMatch.setState(updatedMatch.getState());
        existingMatch.setIsUpdated(updatedMatch.getIsUpdated());
        existingMatch.setParticipants(updatedMatch.getParticipants());
      }
    }
  }
}

