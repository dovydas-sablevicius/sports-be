package com.sourcery.sport.match.service;

import com.sourcery.sport.match.dto.MatchDto;
import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.match.repository.MatchRepository;
import com.sourcery.sport.tournament.model.Tournament;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MatchServiceImpl implements MatchService {

  private final MatchRepository matchRepository;

  public MatchServiceImpl(MatchRepository matchRepository) {
    this.matchRepository = matchRepository;
  }

  @Override
  public void saveMatch(Match match) {
    matchRepository.save(match);
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
    return matchRepository.findByTournament(tournament);
  }

  @Override
  public Match getMatchById(String id) {
    return matchRepository.findById(id).orElse(null);
  }

  @Override
  public void updateMatches(List<Match> updatedMatches) {
    List<Match> matchesToUpdate = matchRepository.findAllById(updatedMatches.stream().map(Match::getMatchId).toList());
    for (Match match : matchesToUpdate) {
      for (Match updatedMatch : updatedMatches) {
        if (match.getMatchId().equals(updatedMatch.getMatchId())) {
          match.setState(updatedMatch.getState());
          match.setIsUpdated(updatedMatch.getIsUpdated());
          match.setParticipants(updatedMatch.getParticipants());
        }
      }
    }
    matchRepository.saveAll(matchesToUpdate);
  }
}
