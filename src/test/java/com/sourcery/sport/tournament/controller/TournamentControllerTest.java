package com.sourcery.sport.tournament.controller;

import com.sourcery.sport.match.dto.MatchDto;
import com.sourcery.sport.match.dto.MatchPlayerDto;
import com.sourcery.sport.mocks.*;
import com.sourcery.sport.tournament.dto.TournamentCreatedDto;
import com.sourcery.sport.tournament.dto.TournamentDto;
import com.sourcery.sport.tournament.dto.TournamentRegisterTeamDto;
import com.sourcery.sport.tournament.dto.TournamentUpdateDto;
import com.sourcery.sport.tournament.model.City;
import com.sourcery.sport.tournament.model.Tournament;
import com.sourcery.sport.tournament.model.TournamentTableType;
import com.sourcery.sport.tournament.model.TournamentType;
import com.sourcery.sport.tournamenttag.model.TournamentTag;
import com.sourcery.sport.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class TournamentControllerTest {

  private CityServiceMock mockCityService;
  private MatchPlayerServiceMock mockMatchPlayerService;
  private MatchServiceMock mockMatchService;
  private TeamServiceMock mockTeamService;
  private TournamentRepositoryMock mockTournamentRepository;
  private TournamentServiceMock mockTournamentService;
  private TournamentTableTypeServiceMock mockTournamentTableTypeService;
  private TournamentTagServiceMock mockTournamentTagService;
  private TournamentTypeServiceMock mockTournamentTypeService;
  private TournamentUserTeamServiceMock mockTournamentUserTeamService;
  private UserServiceMock mockUserService;

  private TournamentController tournamentController;

  private UUID tableTypeId;
  private UUID tournamentTypeId;
  private UUID cityId;
  private UUID tagId;
  private UUID tournamentId;
  private final String userEmail = "user1@mail.com";
  private final String userId = "user1";

  @BeforeEach
  public void setUp() {
    mockCityService = new CityServiceMock();
    mockMatchService = new MatchServiceMock();
    mockTeamService = new TeamServiceMock();
    mockTournamentRepository = new TournamentRepositoryMock();
    mockTournamentTableTypeService = new TournamentTableTypeServiceMock();
    mockTournamentTagService = new TournamentTagServiceMock();
    mockTournamentTypeService = new TournamentTypeServiceMock();
    mockTournamentUserTeamService = new TournamentUserTeamServiceMock();
    mockUserService = new UserServiceMock();
    mockMatchPlayerService = new MatchPlayerServiceMock(mockTeamService, mockUserService);
    mockTournamentService = new TournamentServiceMock(
        mockTournamentTableTypeService,
        mockTournamentTypeService,
        mockCityService,
        mockTournamentTagService
    );

    tournamentController = new TournamentController(
        mockTournamentService,
        mockTournamentTableTypeService,
        mockTournamentTypeService,
        mockCityService,
        mockUserService,
        mockTournamentUserTeamService,
        mockTeamService,
        mockTournamentTagService,
        mockMatchService,
        mockMatchPlayerService
    );

    tableTypeId = UUID.randomUUID();
    tournamentTypeId = UUID.randomUUID();
    cityId = UUID.randomUUID();
    tagId = UUID.randomUUID();
    tournamentId = UUID.randomUUID();

    //Tournament mockTournament = createMockTournament();

    TournamentTableType tableType = new TournamentTableType();
    tableType.setId(tableTypeId);
    mockTournamentTableTypeService.saveTournamentTableType(tableType);

    TournamentType tournamentType = new TournamentType();
    tournamentType.setId(tournamentTypeId);
    mockTournamentTypeService.saveTournamentType(tournamentType);

    City city = new City();
    city.setId(cityId);
    mockCityService.saveCity(city);

    TournamentTag tag = new TournamentTag();
    tag.setId(tagId);
    mockTournamentTagService.saveTag(tag);

    User user = new User();
    user.setEmail(userEmail);
    user.setId(userId);
    mockUserService.addNewUser(user);

    Tournament tournament = new Tournament();
    tournament.setId(tournamentId);
    tournament.setCity(city);
    tournament.setStartDate(LocalDateTime.of(2023, 11, 15, 10, 0, 0, 0));
    tournament.setEndDate(LocalDateTime.of(2026, 11, 15, 18, 0, 0, 0));
    tournament.setTournamentTags(Set.of(tag));
    tournament.setMaxParticipants(100);
    tournament.setAreMatchesGenerated(false);
    mockTournamentService.saveTournament(tournament);
  }

  @Test
  void testCreateTournament_Success() {

    TournamentDto tournamentDto = new TournamentDto();
    tournamentDto.setName("Test Tournament");
    tournamentDto.setTournamentTableTypeId(tableTypeId);
    tournamentDto.setTournamentTypeId(tournamentTypeId);
    tournamentDto.setCityId(cityId);
    tournamentDto.setTagIds(List.of(tagId));

    ResponseEntity<?> response = tournamentController.createTournament(tournamentDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    TournamentCreatedDto tournamentCreatedDto = (TournamentCreatedDto) response.getBody();
    assertEquals("Test Tournament", tournamentCreatedDto.getName());
  }

  @Test
  void testUpdateTournament_Success() {
    TournamentUpdateDto updateTournamentDto = new TournamentUpdateDto();
    updateTournamentDto.setName("Updated Tournament Name");
    updateTournamentDto.setId(tournamentId);
    updateTournamentDto.setTournamentTableTypeId(tableTypeId);
    updateTournamentDto.setTournamentTypeId(tournamentTypeId);
    updateTournamentDto.setCityId(cityId);
    updateTournamentDto.setTagIds(List.of(tagId));

    ResponseEntity<?> response = tournamentController.updateTournament(updateTournamentDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    TournamentUpdateDto tournamentUpdateDto = (TournamentUpdateDto) response.getBody();
    assertEquals("Updated Tournament Name", tournamentUpdateDto.getName());
  }

  @Test
  void testGetAllTournaments_Success() {
    ResponseEntity<?> response = tournamentController.getAllTournaments();

    assertEquals(HttpStatus.OK, response.getStatusCode());

    List<Tournament> tournaments = (List<Tournament>) response.getBody();
      assert tournaments != null;
      assertEquals(tournamentId, tournaments.get(0).getId());
  }

  @Test
  void testGetTournamentById_Success() {
    ResponseEntity<?> response = tournamentController.getTournament(tournamentId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Tournament tournament = (Tournament) response.getBody();
    assertEquals(tournamentId, tournament.getId());
  }

  @Test
  void getTournamentsByCity_Success() {
    ResponseEntity<?> response = tournamentController.getTournamentByCity(cityId);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    List<Tournament> tournaments = (List<Tournament>) response.getBody();
    assert tournaments != null;
    assertEquals(tournamentId, tournaments.get(0).getId());
  }

  @Test
  void getTournamentsByDateRange_Success() {

    LocalDateTime startDate = LocalDateTime.of(2022, 11, 15, 11, 0, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2027, 11, 15, 17, 0, 0, 0);
    ResponseEntity<?> response = tournamentController.getTournamentsByDateRange(startDate, endDate);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    List<Tournament> tournaments = (List<Tournament>) response.getBody();
    assert tournaments != null;
    assertEquals(tournamentId, tournaments.get(0).getId());
  }

  @Test
  void getTournamentsByTags_Success() {
    ResponseEntity<?> response = tournamentController.getTournamentsByTags(List.of(tagId));

    assertEquals(HttpStatus.OK, response.getStatusCode());

    List<Tournament> tournaments = (List<Tournament>) response.getBody();
    assert tournaments != null;
    assertEquals(tournamentId, tournaments.get(0).getId());
  }

  @Test
  void updateTournamentTags_Success() {
    ResponseEntity<?> response = tournamentController.updateTournamentTags(tournamentId, List.of(tagId));

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void registerUserToTournament_Success() {

    Map<String, String> requestBody = new HashMap<>();
    requestBody.put("email", userEmail);

    ResponseEntity<?> response = tournamentController.registerUserToTournament(tournamentId, requestBody);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void registerTeamToTournament_Success() {
    TournamentRegisterTeamDto teamDto = new TournamentRegisterTeamDto();
    teamDto.setTeamName("Team1");
    teamDto.setLeaderId(userId);
    teamDto.setEmails(List.of(userEmail));

    ResponseEntity<?> response = tournamentController.registerTeamToTournament(tournamentId, teamDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void saveMatches_Success() {
    MatchDto matchDto = new MatchDto();
    matchDto.setId("1");
    matchDto.setNextMatchId("2");
    matchDto.setTournamentRoundText("1");
    matchDto.setStartTime("2024-11-11");
    matchDto.setState("PENDING");
    matchDto.setIsUpdated(true);

    MatchPlayerDto player1 = new MatchPlayerDto();
    player1.setId(userId);

    matchDto.setParticipants(List.of(player1));

    ResponseEntity<?> response = tournamentController.saveMatches(tournamentId, List.of(matchDto));
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void getParticipantCount_Success() {
    ResponseEntity<?> response = tournamentController.getParticipantsCount(tournamentId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Long partcipantCount = (Long) response.getBody();
    assertEquals(0, partcipantCount);
  }

}
