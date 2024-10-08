package com.sourcery.sport.match.controller;

import com.sourcery.sport.match.dto.MatchDto;
import com.sourcery.sport.match.dto.MatchPlayerDto;
import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.match.model.MatchPlayer;
import com.sourcery.sport.match.service.MatchPlayerService;
import com.sourcery.sport.match.service.MatchService;
import com.sourcery.sport.tournament.model.Tournament;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<?> updateMatches(@RequestBody List<MatchDto> matchDtos) {
    try {
      Tournament tournament = matchService.getMatchById(matchDtos.get(0).getId()).getTournament();
      List<Match> matches = new ArrayList<>();
      for (MatchDto matchDto : matchDtos) {
        Match match = matchService.mapMatch(matchDto, tournament);
        List<MatchPlayer> matchPlayers = new ArrayList<>();
        for (MatchPlayerDto matchPlayerDto : matchDto.getParticipants()) {
          if (Objects.equals(matchPlayerDto.getId(), "0")) {
            continue;
          }
          MatchPlayer matchPlayer = matchPlayerService.addMatchPlayer(matchPlayerDto, match);
          matchPlayers.add(matchPlayer);
        }
        matchPlayerService.updateMatchPlayers(matchPlayers, match);
        matches.add(match);
      }
      matchService.updateMatches(matches);
      return ResponseEntity.ok("Matches updated successfully!");
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
