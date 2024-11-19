package com.sourcery.sport.match;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sourcery.sport.match.dto.MatchPlayerDto;
import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.match.model.MatchPlayer;
import com.sourcery.sport.match.service.MatchPlayerServiceImpl;
import com.sourcery.sport.team.model.Team;
import com.sourcery.sport.team.service.TeamService;
import com.sourcery.sport.user.model.User;
import com.sourcery.sport.user.service.UserService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MatchPlayerServiceImplTest {

  @Mock
  private TeamService teamService;

  @Mock
  private UserService userService;

  @InjectMocks
  private MatchPlayerServiceImpl matchPlayerService;

  private Match match;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    match = new Match();
  }

  @Test
  void testMapMatchPlayers_StatusNoParty() {
    MatchPlayerDto dto = new MatchPlayerDto();
    dto.setStatus("NO_PARTY");
    List<MatchPlayerDto> dtos = Collections.singletonList(dto);

    List<MatchPlayer> result = matchPlayerService.mapMatchPlayers(dtos, match);

    assertTrue(result.isEmpty(), "Expected no MatchPlayers to be added when status is NO_PARTY");
  }

  @Test
  void testMapMatchPlayers_TypeTeam() {
    MatchPlayerDto dto = new MatchPlayerDto();
    dto.setType("TEAM");
    dto.setId(UUID.randomUUID().toString());
    Team mockTeam = new Team();
    when(teamService.getTeamById(any(UUID.class))).thenReturn(mockTeam);
    List<MatchPlayerDto> dtos = Collections.singletonList(dto);

    List<MatchPlayer> result = matchPlayerService.mapMatchPlayers(dtos, match);

    assertEquals(1, result.size());
    assertEquals(mockTeam, result.get(0).getTeam());
    verify(teamService, times(1)).getTeamById(any(UUID.class));
  }

  @Test
  void testMapMatchPlayers_TypeUser() {
    MatchPlayerDto dto = new MatchPlayerDto();
    dto.setType("USER");
    dto.setId("user123");
    User mockUser = new User();
    when(userService.getUserById("user123")).thenReturn(mockUser);
    List<MatchPlayerDto> dtos = Collections.singletonList(dto);

    List<MatchPlayer> result = matchPlayerService.mapMatchPlayers(dtos, match);

    assertEquals(1, result.size());
    assertEquals(mockUser, result.get(0).getUser());
    verify(userService, times(1)).getUserById("user123");
  }

  @Test
  void testMapMatchPlayers_StatusNotNull() {
    MatchPlayerDto dto = new MatchPlayerDto();
    dto.setType("USER");
    dto.setStatus("ACTIVE");
    dto.setId("user123");
    User mockUser = new User();
    when(userService.getUserById("user123")).thenReturn(mockUser);

    List<MatchPlayerDto> dtos = Collections.singletonList(dto);

    List<MatchPlayer> result = matchPlayerService.mapMatchPlayers(dtos, match);

    assertEquals(1, result.size());
    assertEquals("ACTIVE", result.get(0).getStatus());
  }


  @Test
  void testMapMatchPlayers_StatusNull() {
    MatchPlayerDto dto = new MatchPlayerDto();
    dto.setType("USER");
    dto.setStatus(null);
    dto.setId("user123");
    User mockUser = new User();
    when(userService.getUserById("user123")).thenReturn(mockUser);
    List<MatchPlayerDto> dtos = Collections.singletonList(dto);

    List<MatchPlayer> result = matchPlayerService.mapMatchPlayers(dtos, match);

    assertEquals(1, result.size());
    assertNull(result.get(0).getStatus());
  }

  @Test
  void testMapMatchPlayers_MultipleEntries() {
    MatchPlayerDto dto1 = new MatchPlayerDto();
    dto1.setType("TEAM");
    dto1.setId(UUID.randomUUID().toString());
    dto1.setStatus("ACTIVE");

    MatchPlayerDto dto2 = new MatchPlayerDto();
    dto2.setType("USER");
    dto2.setId("user123");
    dto2.setStatus("NO_PARTY");

    MatchPlayerDto dto3 = new MatchPlayerDto();
    dto3.setType("USER");
    dto3.setId("user456");
    dto3.setStatus("INACTIVE");

    Team mockTeam = new Team();
    User mockUser1 = new User();
    User mockUser2 = new User();

    when(teamService.getTeamById(any(UUID.class))).thenReturn(mockTeam);
    when(userService.getUserById("user123")).thenReturn(mockUser1);
    when(userService.getUserById("user456")).thenReturn(mockUser2);

    List<MatchPlayerDto> dtos = Arrays.asList(dto1, dto2, dto3);

    List<MatchPlayer> result = matchPlayerService.mapMatchPlayers(dtos, match);

    assertEquals(2, result.size());
    assertEquals(mockTeam, result.get(0).getTeam());
    assertEquals("ACTIVE", result.get(0).getStatus());
    assertEquals(mockUser2, result.get(1).getUser());
    assertEquals("INACTIVE", result.get(1).getStatus());
  }
}
