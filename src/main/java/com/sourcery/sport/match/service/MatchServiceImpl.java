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
    matchesToUpdate.forEach(match -> {
      updatedMatches.stream()
          .filter(updatedMatch -> match.getMatchId().equals(updatedMatch.getMatchId()))
          .findFirst()
          .ifPresent(updatedMatch -> updateMatchDetails(match, updatedMatch));
    });
    matchRepository.saveAll(matchesToUpdate);
  }

  private void updateMatchDetails(Match match, Match updatedMatch) {
    match.setState(updatedMatch.getState());
    match.setIsUpdated(updatedMatch.getIsUpdated());
    match.setParticipants(updatedMatch.getParticipants());
  }
}

// Refactoring Analysis:
// 1. SRP (Single Responsibility Principle):
//    - Extracted `updateMatchDetails` method to ensure that `updateMatches` focuses only on orchestrating the update process, while `updateMatchDetails` handles the details of updating a match.
// 2. DRY (Don't Repeat Yourself):
//    - The logic to update fields of `Match` was moved to a dedicated method (`updateMatchDetails`), avoiding repetition and making future modifications easier.
// 3. Law of Demeter (Principle of Least Knowledge):
//    - Used `.ifPresent()` to limit the direct exposure of updatedMatches list when finding and updating a match, thereby reducing tight coupling.
// 4. Iterator Pattern (GRASP):
//    - Utilized `forEach` on `matchesToUpdate` to iterate over matches more declaratively, simplifying the control flow and enhancing readability.
