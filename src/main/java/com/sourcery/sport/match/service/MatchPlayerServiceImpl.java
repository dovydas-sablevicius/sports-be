package com.sourcery.sport.match.service;

import com.sourcery.sport.match.dto.MatchPlayerDto;
import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.match.model.MatchPlayer;
import com.sourcery.sport.match.repository.MatchPlayerRepository;
import com.sourcery.sport.team.service.TeamService;
import com.sourcery.sport.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MatchPlayerServiceImpl implements MatchPlayerService {

  private final MatchPlayerRepository matchPlayerRepository;
  private final TeamService teamService;
  private final UserService userService;

  public MatchPlayerServiceImpl(MatchPlayerRepository matchPlayerRepository, TeamService teamService,
                                UserService userService) {
    this.matchPlayerRepository = matchPlayerRepository;
    this.teamService = teamService;
    this.userService = userService;
  }

  @Override
  public void saveMatchPlayers(List<MatchPlayer> matchPlayers) {
    matchPlayerRepository.saveAll(matchPlayers);
  }

  // Refactor
  @Override
  public List<MatchPlayer> mapMatchPlayers(List<MatchPlayerDto> matchPlayerDtos, Match match) {
    List<MatchPlayer> matchPlayers = new ArrayList<>();
    for (MatchPlayerDto matchPlayerDto : matchPlayerDtos) {
      MatchPlayer matchPlayer = new MatchPlayer();
      matchPlayer.setId(UUID.randomUUID());
      matchPlayer.setMatch(match);
      matchPlayer.setScore(matchPlayerDto.getScore());
      matchPlayer.setIsWinner(matchPlayerDto.getIsWinner());

      if (matchPlayerDto.getStatus() != null && matchPlayerDto.getStatus().equals("NO_PARTY")) {
        continue;
      }

      if (matchPlayerDto.getType().equals("TEAM")) {
        matchPlayer.setTeam(teamService.getTeamById(UUID.fromString(matchPlayerDto.getId())));
      } else {
        matchPlayer.setUser(userService.getUserById(matchPlayerDto.getId()));
      }

      if (Objects.nonNull(matchPlayerDto.getStatus())) {
        matchPlayer.setStatus(matchPlayerDto.getStatus());
      }

      matchPlayers.add(matchPlayer);
    }
    return matchPlayers;
  }

  @Override
  public MatchPlayer addMatchPlayer(MatchPlayerDto matchPlayerDto, Match match) {
    MatchPlayer matchPlayer = new MatchPlayer();
    matchPlayer.setId(UUID.randomUUID());
    matchPlayer.setMatch(match);
    matchPlayer.setScore(matchPlayerDto.getScore());
    matchPlayer.setIsWinner(matchPlayerDto.getIsWinner());

    if (matchPlayerDto.getType().equals("TEAM")) {
      matchPlayer.setTeam(teamService.getTeamById(UUID.fromString(matchPlayerDto.getId())));
    } else {
      matchPlayer.setUser(userService.getUserById(matchPlayerDto.getId()));
    }

    if (Objects.nonNull(matchPlayerDto.getStatus())) {
      matchPlayer.setStatus(matchPlayerDto.getStatus());
    }

    return matchPlayer;
  }

  @Override
  public void updateMatchPlayers(List<MatchPlayer> matchPlayer, Match match) {
    List<MatchPlayer> matchPlayers = matchPlayerRepository.findByMatch(match);
    matchPlayerRepository.deleteAll(matchPlayers);
    matchPlayerRepository.saveAll(matchPlayer);
  }

  @Override
  public boolean isMatchPlayerInMatch(MatchPlayer matchPlayer, Match match) {
    return matchPlayerRepository.findByMatch(match).contains(matchPlayer);
  }
}
