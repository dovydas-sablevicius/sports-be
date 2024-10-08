package com.sourcery.sport.team.service;

import com.sourcery.sport.team.model.Team;
import com.sourcery.sport.team.repository.TeamRepository;
import com.sourcery.sport.tournament.repository.TournamentUserTeamRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {

  private final TeamRepository teamRepository;
  private final TournamentUserTeamRepository tournamentUserTeamRepository;

  public TeamServiceImpl(TeamRepository teamRepository, TournamentUserTeamRepository tournamentUserTeamRepository) {
    this.teamRepository = teamRepository;
    this.tournamentUserTeamRepository = tournamentUserTeamRepository;
  }

  @Override
  public Team createTeam(String name) {
    Team team = new Team();
    team.setName(name);
    return teamRepository.save(team);
  }

  @Override
  public Team getTeam(String name) {
    return teamRepository.findByName(name);
  }

  @Override
  public Team getTeamById(UUID id) {
    return teamRepository.findById(id).orElse(null);
  }

  @Override
  public Team saveTeam(Team team) {
    return teamRepository.save(team);
  }

  @Override
  public void removeTeamFromTournament(UUID tournamentId, UUID teamId) {
    tournamentUserTeamRepository.deleteByTournamentIdAndTeamId(tournamentId, teamId);
    teamRepository.deleteById(teamId);
  }
}
