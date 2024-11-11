package com.sourcery.sport.mocks;

import com.sourcery.sport.match.dto.MatchPlayerDto;
import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.match.model.MatchPlayer;
import com.sourcery.sport.match.service.MatchPlayerService;
import java.util.*;
import java.util.stream.Collectors;

public class MatchPlayerServiceMock implements MatchPlayerService {

  // Mock repository using a Map (MatchPlayer -> UUID)
  private final Map<MatchPlayer, UUID> matchPlayerRepository = new HashMap<>();
  private final TeamServiceMock teamService;
  private final UserServiceMock userService;

  // Constructor to inject mocked services
  public MatchPlayerServiceMock(TeamServiceMock teamService, UserServiceMock userService) {
    this.teamService = teamService;
    this.userService = userService;
  }

  @Override
  public void saveMatchPlayers(List<MatchPlayer> matchPlayers) {
    // Simulate saving multiple match players in the repository
    for (MatchPlayer matchPlayer : matchPlayers) {
      matchPlayerRepository.put(matchPlayer, matchPlayer.getId());
    }
  }

  @Override
  public List<MatchPlayer> mapMatchPlayers(List<MatchPlayerDto> matchPlayerDtos, Match match) {
    List<MatchPlayer> matchPlayers = new ArrayList<>();
    for (MatchPlayerDto matchPlayerDto : matchPlayerDtos) {
      // Creating MatchPlayer from DTO and setting its properties
      MatchPlayer matchPlayer = new MatchPlayer();
      matchPlayer.setId(UUID.randomUUID());
      matchPlayer.setMatch(match);

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

    // Set team or user based on type
    if (matchPlayerDto.getType().equals("TEAM")) {
      matchPlayer.setTeam(teamService.getTeamById(UUID.fromString(matchPlayerDto.getId()))); // Mocked call to TeamServiceMock
    } else {
      matchPlayer.setUser(userService.getUserById(matchPlayerDto.getId())); // Mocked call to UserServiceMock
    }

    // Set status if available
    if (Objects.nonNull(matchPlayerDto.getStatus())) {
      matchPlayer.setStatus(matchPlayerDto.getStatus());
    }

    return matchPlayer;
  }

  @Override
  public void updateMatchPlayers(List<MatchPlayer> matchPlayers, Match match) {
    // First, delete the existing match players for the match from the map
    List<MatchPlayer> existingMatchPlayers = findMatchPlayersByMatch(match);
    for (MatchPlayer existingMatchPlayer : existingMatchPlayers) {
      matchPlayerRepository.remove(existingMatchPlayer);
    }

    // Now, save the new list of match players
    saveMatchPlayers(matchPlayers);
  }

  @Override
  public boolean isMatchPlayerInMatch(MatchPlayer matchPlayer, Match match) {
    // Check if the matchPlayer is in the match (using the map)
    return findMatchPlayersByMatch(match).contains(matchPlayer);
  }

  // Helper method to find MatchPlayers by Match
  private List<MatchPlayer> findMatchPlayersByMatch(Match match) {
    return matchPlayerRepository.keySet().stream()
        .filter(matchPlayer -> matchPlayer.getMatch().equals(match))
        .collect(Collectors.toList());
  }
}

