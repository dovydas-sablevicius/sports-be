package com.sourcery.sport.match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.match.repository.MatchRepository;
import com.sourcery.sport.match.service.MatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

class MatchServiceImplTest {

  @Mock
  private MatchRepository matchRepository;

  @InjectMocks
  private MatchServiceImpl matchService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void updateMatches_ShouldProcessAllMatchesInTheList() {
    Match repoMatch1 = Match.builder()
        .matchId("1")
        .state("Pending")
        .isUpdated(false)
        .build();
    Match repoMatch2 = Match.builder()
        .matchId("2")
        .state("Pending")
        .isUpdated(false)
        .build();
    when(matchRepository.findAllById(List.of("1", "2"))).thenReturn(List.of(repoMatch1, repoMatch2));

    Match updatedMatch1 = Match.builder()
        .matchId("1")
        .state("Completed")
        .isUpdated(true)
        .build();
    Match updatedMatch2 = Match.builder()
        .matchId("2")
        .state("In Progress")
        .isUpdated(true)
        .build();

    matchService.updateMatches(List.of(updatedMatch1, updatedMatch2));

    assertEquals("Completed", repoMatch1.getState());
    assertEquals("In Progress", repoMatch2.getState());
    assertEquals(true, repoMatch1.getIsUpdated());
    assertEquals(true, repoMatch2.getIsUpdated());

    verify(matchRepository).saveAll(List.of(repoMatch1, repoMatch2));
  }

  @Test
  void updateMatches_ShouldNotUpdateWhenMatchIdsDoNotMatch() {
    Match repoMatch = Match.builder()
        .matchId("1")
        .state("Pending")
        .isUpdated(false)
        .build();
    when(matchRepository.findAllById(List.of("1"))).thenReturn(List.of(repoMatch));

    Match updatedMatch = Match.builder()
        .matchId("2") // Mismatched ID
        .state("Completed")
        .isUpdated(true)
        .build();

    matchService.updateMatches(List.of(updatedMatch));

    assertEquals("Pending", repoMatch.getState());
    assertEquals(false, repoMatch.getIsUpdated());
  }

  @Test
  void updateMatches_ShouldNotPartiallyUpdateMatches() {
    Match repoMatch1 = Match.builder()
        .matchId("1")
        .state("Pending")
        .isUpdated(false)
        .build();
    Match repoMatch2 = Match.builder()
        .matchId("2")
        .state("Pending")
        .isUpdated(false)
        .build();
    when(matchRepository.findAllById(List.of("1", "2"))).thenReturn(List.of(repoMatch1, repoMatch2));

    Match updatedMatch = Match.builder()
        .matchId("1")
        .state("Completed")
        .isUpdated(true)
        .build();

    matchService.updateMatches(List.of(updatedMatch));

    assertEquals("Pending", repoMatch1.getState());
    assertEquals(false, repoMatch1.getIsUpdated());
    assertEquals("Pending", repoMatch2.getState());
    assertEquals(false, repoMatch2.getIsUpdated());
  }

  @Test
  void updateMatches_ShouldDoNothingIfNoMatchesFound() {
    when(matchRepository.findAllById(anyList())).thenReturn(List.of());

    Match updatedMatch = Match.builder()
        .matchId("1")
        .state("Completed")
        .isUpdated(true)
        .build();

    matchService.updateMatches(List.of(updatedMatch));

    verify(matchRepository).findAllById(anyList());
  }

}
