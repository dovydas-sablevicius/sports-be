package com.sourcery.sport.tournament.service;


import com.sourcery.sport.team.dto.TeamDto;
import com.sourcery.sport.team.model.Team;
import com.sourcery.sport.tournament.model.Tournament;
import com.sourcery.sport.tournament.model.TournamentUserTeam;
import com.sourcery.sport.tournament.model.TournamentUserTeam.TournamentUserTeamId;
import com.sourcery.sport.tournament.repository.TournamentUserTeamRepository;
import com.sourcery.sport.user.dto.UserDto;
import com.sourcery.sport.user.model.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;


@Service
public class TournamentUserTeamServiceImpl implements TournamentUserTeamService {


  private final TournamentUserTeamRepository tournamentUserTeamRepository;

  public TournamentUserTeamServiceImpl(TournamentUserTeamRepository tournamentUserTeamRepository) {
    this.tournamentUserTeamRepository = tournamentUserTeamRepository;
  }

  @Override
  public List<TournamentUserTeam> findByUserId(String userId) {
    return tournamentUserTeamRepository.findByUserId(userId);
  }

  @Override
  public List<TournamentUserTeam> findByTournamentId(UUID tournamentId) {
    return tournamentUserTeamRepository.findByTournamentId(tournamentId);
  }

  @Override
  public void saveTournamentUserTeam(TournamentUserTeam tournamentUserTeam) {
    tournamentUserTeamRepository.save(tournamentUserTeam);
  }

  @Override
  public void addUserToTournament(User user, Tournament tournament) {
    TournamentUserTeamId id = new TournamentUserTeamId();
    id.setTournamentId(tournament.getId());
    id.setUserId(user.getId());
    TournamentUserTeam tournamentUserTeam = new TournamentUserTeam();
    tournamentUserTeam.setId(id);
    tournamentUserTeam.setTournament(tournament);
    tournamentUserTeam.setUser(user);
    tournamentUserTeamRepository.save(tournamentUserTeam);
  }

  @Override
  public void addUserTeamToTournament(User user, Tournament tournament, Team team) {
    TournamentUserTeamId id = new TournamentUserTeamId(user.getId(), tournament.getId());
    TournamentUserTeam tournamentUserTeam = new TournamentUserTeam();
    tournamentUserTeam.setId(id);
    tournamentUserTeam.setTournament(tournament);
    tournamentUserTeam.setUser(user);
    tournamentUserTeam.setTeam(team);
    tournamentUserTeamRepository.save(tournamentUserTeam);
  }

  @Override
  public List<UserDto> getPlayersByTournamentId(UUID tournamentId) {
    return tournamentUserTeamRepository.findByTournamentId(tournamentId).stream()
        .map(TournamentUserTeam::getUser)
        .map(user -> new UserDto(user.getId(), user.getName(),
            user.getSurname(), user.getEmail(),
            user.getImage(), user.getPhoneNumber(),
            user.getCity().getId()))
        .toList();
  }

  @Override
  public List<TeamDto> getTeamsByTournamentId(UUID tournamentId) {
    List<TournamentUserTeam> tournamentUserTeams = tournamentUserTeamRepository.findByTournamentId(tournamentId);
    List<TeamDto> teams = new ArrayList<>();
    Set<UUID> teamIds = new HashSet<>();
    for (TournamentUserTeam tournamentUserTeam : tournamentUserTeams) {
      if (teamIds.contains(tournamentUserTeam.getTeam().getId())) {
        continue;
      }
      TeamDto teamDto = new TeamDto();
      teamDto.setId(tournamentUserTeam.getTeam().getId());
      teamDto.setName(tournamentUserTeam.getTeam().getName());
      teamDto.setLeaderId(tournamentUserTeam.getTeam().getLeader().getId());
      teamDto.setUsers(tournamentUserTeam.getTeam().getUsers().stream()
          .map(user -> new UserDto(user.getId(), user.getName(),
              user.getSurname(), user.getEmail(), user.getImage(),
              user.getPhoneNumber(), user.getCity().getId()))
          .toList());
      teams.add(teamDto);
      teamIds.add(tournamentUserTeam.getTeam().getId());
    }
    return teams;
  }

  @Override
  public List<Tournament> getTournamentsByUserId(String userId) {
    List<TournamentUserTeam> tournamentUserTeams = tournamentUserTeamRepository.findByUserId(userId);
    List<Tournament> tournaments = new ArrayList<>();
    for (TournamentUserTeam tournamentUserTeam : tournamentUserTeams) {
      tournaments.add(tournamentUserTeam.getTournament());
    }
    return tournaments;
  }

  @Override
  public long getParticipantsCountByTournamentId(UUID tournamentId) {
    return tournamentUserTeamRepository.countByTournamentId(tournamentId);
  }

  @Override
  public void removeUserFromTournament(String userId, UUID tournamentId) {
    tournamentUserTeamRepository.deleteByTournamentIdAndUserId(userId, tournamentId);
  }
}
