package com.sourcery.sport.mocks;

import com.sourcery.sport.team.model.Team;
import com.sourcery.sport.team.service.TeamService;
import com.sourcery.sport.tournament.model.TournamentUserTeam;
import com.sourcery.sport.tournament.model.TournamentUserTeam.TournamentUserTeamId;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TeamServiceMock implements TeamService {
  private final Map<Team, UUID> teamDatabase = new HashMap<>(); // For teams (Team -> UUID)
  private final Map<TournamentUserTeam, TournamentUserTeamId> tournamentUserTeamDatabase = new HashMap<>(); // For TournamentUserTeams (TournamentUserTeam -> TournamentUserTeamId)

  @Override
  public Team createTeam(String name) {
    Team team = new Team();
    team.setName(name);
    // Simulate assigning an ID
    UUID teamId = UUID.randomUUID();
    team.setId(teamId);

    // Save to the team database (Map)
    teamDatabase.put(team, teamId);

    return team;
  }

  @Override
  public Team getTeam(String name) {
    // Find the team by name in the team database (map) and return it
    return teamDatabase.keySet().stream()
        .filter(team -> team.getName().equals(name))
        .findFirst()
        .orElse(null);
  }

  @Override
  public Team getTeamById(UUID id) {
    // Find the team by UUID in the team database (map)
    return teamDatabase.keySet().stream()
        .filter(team -> team.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  @Override
  public Team saveTeam(Team team) {
    // Save or update the team in the map (teamDatabase)
    teamDatabase.put(team, team.getId());
    return team;
  }

  @Override
  public void removeTeamFromTournament(UUID tournamentId, UUID teamId) {
    // Remove the associated TournamentUserTeam entries from the tournamentUserTeamDatabase map
    tournamentUserTeamDatabase.entrySet().removeIf(entry ->
        entry.getKey().getTournament().getId().equals(tournamentId) &&
            entry.getKey().getTeam().getId().equals(teamId)  // Accessing the teamId from the 'team' field in the TournamentUserTeam
    );
  }

}

