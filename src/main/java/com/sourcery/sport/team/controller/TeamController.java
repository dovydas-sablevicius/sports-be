package com.sourcery.sport.team.controller;

import com.sourcery.sport.team.service.TeamService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
public class TeamController {

  private final TeamService teamService;

  public TeamController(TeamService teamService) {
    this.teamService = teamService;
  }

  @DeleteMapping("/{tournamentId}/remove-team/{teamId}")
  public ResponseEntity<?> removeTeamFromTournament(@PathVariable UUID tournamentId, @PathVariable UUID teamId) {
    try {
      teamService.removeTeamFromTournament(tournamentId, teamId);
      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
