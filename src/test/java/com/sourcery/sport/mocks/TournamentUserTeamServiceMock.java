package com.sourcery.sport.mocks;

import com.sourcery.sport.team.dto.TeamDto;
import com.sourcery.sport.team.model.Team;
import com.sourcery.sport.tournament.model.Tournament;
import com.sourcery.sport.tournament.model.TournamentUserTeam;
import com.sourcery.sport.tournament.model.TournamentUserTeam.TournamentUserTeamId;
import com.sourcery.sport.tournament.service.TournamentUserTeamService;
import com.sourcery.sport.user.dto.UserDto;
import com.sourcery.sport.user.model.User;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentUserTeamServiceMock implements TournamentUserTeamService {

  // Map to simulate the repository
  private final Map<TournamentUserTeamId, TournamentUserTeam> database = new HashMap<>();

  @Override
  public List<TournamentUserTeam> findByUserId(String userId) {
    // Filtering based on userId
    return database.values().stream()
        .filter(t -> t.getId().getUserId().equals(userId))
        .collect(Collectors.toList());
  }

  @Override
  public List<TournamentUserTeam> findByTournamentId(UUID tournamentId) {
    // Filtering based on tournamentId
    return database.values().stream()
        .filter(t -> t.getId().getTournamentId().equals(tournamentId))
        .collect(Collectors.toList());
  }

  @Override
  public void saveTournamentUserTeam(TournamentUserTeam tournamentUserTeam) {
    // Saving to the map (using TournamentUserTeamId as key)
    database.put(tournamentUserTeam.getId(), tournamentUserTeam);
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

    // Save to map
    saveTournamentUserTeam(tournamentUserTeam);
  }

  @Override
  public void addUserTeamToTournament(User user, Tournament tournament, Team team) {
    TournamentUserTeamId id = new TournamentUserTeamId(user.getId(), tournament.getId());
    TournamentUserTeam tournamentUserTeam = new TournamentUserTeam();
    tournamentUserTeam.setId(id);
    tournamentUserTeam.setTournament(tournament);
    tournamentUserTeam.setUser(user);
    tournamentUserTeam.setTeam(team);

    // Save to map
    saveTournamentUserTeam(tournamentUserTeam);
  }

  @Override
  public List<UserDto> getPlayersByTournamentId(UUID tournamentId) {
    return findByTournamentId(tournamentId).stream()
        .map(TournamentUserTeam::getUser)
        .map(user -> new UserDto(user.getId(), user.getName(), user.getSurname(),
            user.getEmail(), user.getImage(), user.getPhoneNumber(), user.getCity().getId()))
        .collect(Collectors.toList());
  }

  @Override
  public List<TeamDto> getTeamsByTournamentId(UUID tournamentId) {
    List<TournamentUserTeam> tournamentUserTeams = findByTournamentId(tournamentId);
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
          .collect(Collectors.toList()));
      teams.add(teamDto);
      teamIds.add(tournamentUserTeam.getTeam().getId());
    }
    return teams;
  }

  @Override
  public List<Tournament> getTournamentsByUserId(String userId) {
    return findByUserId(userId).stream()
        .map(TournamentUserTeam::getTournament)
        .collect(Collectors.toList());
  }

  @Override
  public long getParticipantsCountByTournamentId(UUID tournamentId) {
    return findByTournamentId(tournamentId).size();
  }

  @Override
  public void removeUserFromTournament(String userId, UUID tournamentId) {
    // Remove entry from map by userId and tournamentId
    database.entrySet().removeIf(entry ->
        entry.getValue().getId().getUserId().equals(userId) &&
            entry.getValue().getId().getTournamentId().equals(tournamentId)
    );
  }
}
