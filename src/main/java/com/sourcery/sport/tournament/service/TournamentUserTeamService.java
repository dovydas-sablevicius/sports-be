package com.sourcery.sport.tournament.service;

import com.sourcery.sport.team.dto.TeamDto;
import com.sourcery.sport.team.model.Team;
import com.sourcery.sport.tournament.model.Tournament;
import com.sourcery.sport.tournament.model.TournamentUserTeam;
import com.sourcery.sport.user.dto.UserDto;
import com.sourcery.sport.user.model.User;
import java.util.List;
import java.util.UUID;

public interface TournamentUserTeamService {
  List<TournamentUserTeam> findByUserId(String userId);

  List<TournamentUserTeam> findByTournamentId(UUID tournamentId);

  void saveTournamentUserTeam(TournamentUserTeam tournamentUserTeam);

  void addUserToTournament(User user, Tournament tournament);

  void addUserTeamToTournament(User user, Tournament tournament, Team team);

  List<UserDto> getPlayersByTournamentId(UUID tournamentId);

  List<TeamDto> getTeamsByTournamentId(UUID tournamentId);

  List<Tournament> getTournamentsByUserId(String userId);

  long getParticipantsCountByTournamentId(UUID tournamentId);

  void removeUserFromTournament(String userId, UUID tournamentId);
}
