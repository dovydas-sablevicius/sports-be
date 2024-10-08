package com.sourcery.sport.team.service;

import com.sourcery.sport.team.model.Team;
import java.util.UUID;

public interface TeamService {
  Team createTeam(String name);

  Team getTeam(String name);

  Team getTeamById(UUID id);

  Team saveTeam(Team team);

  void removeTeamFromTournament(UUID tournamentId, UUID teamId);
}
