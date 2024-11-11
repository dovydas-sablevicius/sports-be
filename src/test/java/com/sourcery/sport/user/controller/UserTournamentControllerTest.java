package com.sourcery.sport.user.controller;

import static com.sourcery.sport.utils.UserFactory.CreateMockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sourcery.sport.mocks.UserServiceMock;
import com.sourcery.sport.user.dto.FavoriteTournamentDto;
import com.sourcery.sport.user.dto.UserStatisticsDto;
import com.sourcery.sport.user.model.User;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserTournamentControllerTest {

  private UserServiceMock mockUserService;
  private UserTournamentController userTournamentController;


  @BeforeEach
  public void setUp() {
    mockUserService = new UserServiceMock();
    userTournamentController = new UserTournamentController(mockUserService);
  }

  @Test
  void testGetFavoriteTournament_Success() {
    UUID favoriteTournament = UUID.randomUUID();
    User mockUser = CreateMockUser();
    mockUserService.updateUserFavoriteTournament(mockUser.getId(), favoriteTournament);
    ResponseEntity<FavoriteTournamentDto> response = userTournamentController.getFavoriteTournament(mockUser.getId());
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(response.getBody().getTournamentUUID(), favoriteTournament);
  }

  @Test
  void testGetFavoriteTournament_NoFavoriteTournament() {
    User mockUser = CreateMockUser();
    ResponseEntity<FavoriteTournamentDto> response = userTournamentController.getFavoriteTournament(mockUser.getId());
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(response.getBody().getTournamentUUID(), new UUID(0, 0));
  }

  @Test
  void testGetUserStatistics() {
    User mockUser = CreateMockUser();
    Integer participatingCount = 4;
    Integer participatedCount = 9;
    Integer wonCount = 1;
    UUID favoriteTournament = UUID.randomUUID();
    mockUserService.updateUserFavoriteTournament(mockUser.getId(), favoriteTournament);
    mockUserService.updateParticipatingCount(mockUser.getId(), participatingCount);
    mockUserService.updateWonCount(mockUser.getId(), wonCount);
    mockUserService.updateParticipationCount(mockUser.getId(), participatedCount);

    ResponseEntity<UserStatisticsDto> response = userTournamentController.getUserStatistics(mockUser.getId());

    assertNotNull(response);
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    UserStatisticsDto statistics = response.getBody();
    assertNotNull(statistics);
    assertEquals(participatedCount, statistics.getParticipated());
    assertEquals(participatingCount, statistics.getParticipating());
    assertEquals(wonCount, statistics.getWon());
  }
}