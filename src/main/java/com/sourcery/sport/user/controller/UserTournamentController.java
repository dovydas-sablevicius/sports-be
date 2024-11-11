package com.sourcery.sport.user.controller;

import com.sourcery.sport.user.dto.FavoriteTournamentDto;
import com.sourcery.sport.user.dto.UserStatisticsDto;
import com.sourcery.sport.user.service.UserService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserTournamentController {

  private final UserService userService;

  public UserTournamentController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}/statistics")
  public ResponseEntity<UserStatisticsDto> getUserStatistics(@PathVariable String id) {
    Integer participated = userService.getParticipatedTournamentsCount(id);
    Integer participating = userService.getParticipatingTournamentsCount(id);
    Integer won = userService.getWonTournamentsCount(id);
    UserStatisticsDto userStatisticsDto = new UserStatisticsDto(participated, participating, won);
    return ResponseEntity.ok(userStatisticsDto);
  }

  @GetMapping("/{id}/favorite-tournament")
  public ResponseEntity<FavoriteTournamentDto> getFavoriteTournament(@PathVariable String id) {
    UUID favoriteTournamentId = userService.getFavoriteTournamentType(id);
    FavoriteTournamentDto favoriteTournamentDto = new FavoriteTournamentDto(favoriteTournamentId);
    return ResponseEntity.ok(favoriteTournamentDto);
  }
}
