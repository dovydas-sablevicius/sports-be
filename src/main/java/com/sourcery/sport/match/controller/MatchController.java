package com.sourcery.sport.match.controller;

import com.sourcery.sport.match.dto.MatchDto;
import com.sourcery.sport.match.dto.MatchPlayerDto;
import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.match.model.MatchPlayer;
import com.sourcery.sport.match.model.UpdateMatchResponse;
import com.sourcery.sport.match.service.MatchPlayerService;
import com.sourcery.sport.match.service.MatchService;
import com.sourcery.sport.tournament.model.Tournament;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/matches")
public class MatchController {

  private final MatchService matchService;
  private final MatchPlayerService matchPlayerService;

  public MatchController(MatchService matchService, MatchPlayerService matchPlayerService) {
    this.matchService = matchService;
    this.matchPlayerService = matchPlayerService;
  }

  @PreAuthorize("hasAuthority('admin')")
  @PutMapping("/updateMatches")
  public ResponseEntity<UpdateMatchResponse> updateMatches(@Valid @RequestBody List<MatchDto> matchDtos) {
    try {
      Tournament tournament = matchService.getMatchById(matchDtos.get(0).getId()).getTournament();
      List<Match> matches = matchDtos.stream()
          .map(matchDto -> mapMatchWithPlayers(matchDto, tournament))
          .toList();

      matchService.updateMatches(matches);
      return ResponseEntity.ok(new UpdateMatchResponse("Matches updated successfully!"));
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(new UpdateMatchResponse(ex.getMessage()));
    }
  }

  private Match mapMatchWithPlayers(MatchDto matchDto, Tournament tournament) {
    Match match = matchService.mapMatch(matchDto, tournament);
    List<MatchPlayer> matchPlayers = matchDto.getParticipants().stream()
        .filter(this::isValidParticipant)
        .map(matchPlayerDto -> matchPlayerService.addMatchPlayer(matchPlayerDto, match))
        .toList();

    matchPlayerService.updateMatchPlayers(matchPlayers, match);
    return match;
  }

  private boolean isValidParticipant(MatchPlayerDto matchPlayerDto) {
    return matchPlayerDto != null && !Objects.equals(matchPlayerDto.getId(), "0");
  }
}