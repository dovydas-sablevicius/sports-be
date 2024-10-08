package com.sourcery.sport.tournament.controller;

import com.sourcery.sport.match.dto.MatchDto;
import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.match.model.MatchPlayer;
import com.sourcery.sport.match.service.MatchPlayerService;
import com.sourcery.sport.match.service.MatchService;
import com.sourcery.sport.team.dto.TeamDto;
import com.sourcery.sport.team.model.Team;
import com.sourcery.sport.team.service.TeamService;
import com.sourcery.sport.tournament.dto.TournamentCreatedDto;
import com.sourcery.sport.tournament.dto.TournamentDto;
import com.sourcery.sport.tournament.dto.TournamentRegisterTeamDto;
import com.sourcery.sport.tournament.dto.TournamentUpdateDto;
import com.sourcery.sport.tournament.model.City;
import com.sourcery.sport.tournament.model.Tournament;
import com.sourcery.sport.tournament.model.TournamentTableType;
import com.sourcery.sport.tournament.model.TournamentType;
import com.sourcery.sport.tournament.service.CityService;
import com.sourcery.sport.tournament.service.TournamentService;
import com.sourcery.sport.tournament.service.TournamentTableTypeService;
import com.sourcery.sport.tournament.service.TournamentTypeService;
import com.sourcery.sport.tournament.service.TournamentUserTeamService;
import com.sourcery.sport.tournamenttag.model.TournamentTag;
import com.sourcery.sport.tournamenttag.service.TournamentTagService;
import com.sourcery.sport.user.dto.UserDto;
import com.sourcery.sport.user.model.User;
import com.sourcery.sport.user.service.UserService;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {

  private final TournamentService tournamentService;
  private final TournamentTableTypeService tournamentTableTypeService;
  private final TournamentTypeService tournamentTypeService;
  private final CityService cityService;
  private final TournamentTagService tournamentTagService;
  private final UserService userService;
  private final TournamentUserTeamService tournamentUserTeamService;
  private final TeamService teamService;
  private final MatchService matchService;
  private final MatchPlayerService matchPlayerService;

  public TournamentController(TournamentService tournamentService,
                              TournamentTableTypeService tournamentTableTypeService,
                              TournamentTypeService tournamentTypeService,
                              CityService cityService, UserService userService,
                              TournamentUserTeamService tournamentUserTeamService,
                              TeamService teamService, TournamentTagService tournamentTagService,
                              MatchService matchService, MatchPlayerService matchPlayerService) {
    this.tournamentService = tournamentService;
    this.tournamentTableTypeService = tournamentTableTypeService;
    this.tournamentTypeService = tournamentTypeService;
    this.cityService = cityService;
    this.tournamentTagService = tournamentTagService;
    this.userService = userService;
    this.tournamentUserTeamService = tournamentUserTeamService;
    this.teamService = teamService;
    this.matchService = matchService;
    this.matchPlayerService = matchPlayerService;
  }

  @PreAuthorize("hasAuthority('admin')")
  @PostMapping("/create-tournament")
  @ResponseBody
  public ResponseEntity<?> createTournament(@RequestBody TournamentDto tournamentDto) {
    try {
      if (tournamentDto.getTournamentTableTypeId() == null
          ||
          tournamentDto.getTournamentTypeId() == null
          ||
          tournamentDto.getCityId() == null) {
        throw new IllegalArgumentException("One or more required fields are missing.");
      }

      Tournament tournament = tournamentService.saveTournament(toTournament(tournamentDto));
      TournamentCreatedDto tournamentCreated = new TournamentCreatedDto(tournament.getId(), tournament.getName(),
          tournament.getDescription(), tournament.getPrizes(),
          tournament.getStartDate(), tournament.getEndDate(),
          tournament.getMaxParticipants(), tournament.getTournamentTableType().getId(),
          tournament.getTournamentType().getId(), tournament.getCity().getId(),
          tournament.getParticipationType(), tournament.getTeamSize(), tournament.getTournamentTagsIds());
      return ResponseEntity.ok(tournamentCreated);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PreAuthorize("hasAuthority('admin')")
  @PutMapping("/update-tournament")
  @ResponseBody
  public ResponseEntity<?> updateTournament(@RequestBody TournamentUpdateDto tournamentDto) {
    UUID tournamentId = tournamentDto.getId();

    Tournament existingTournament = tournamentService.getTournamentById(tournamentId);

    if (existingTournament == null) {
      return ResponseEntity.notFound().build();
    }

    Tournament updatedTournament = tournamentService.updateTournament(existingTournament, tournamentDto);

    TournamentUpdateDto tournamentUpdated = new TournamentUpdateDto(updatedTournament.getId(),
        updatedTournament.getName(),
        updatedTournament.getDescription(),
        updatedTournament.getPrizes(),
        updatedTournament.getStartDate(),
        updatedTournament.getEndDate(),
        updatedTournament.getMaxParticipants(),
        updatedTournament.getTournamentTableType().getId(),
        updatedTournament.getTournamentType().getId(),
        updatedTournament.getCity().getId(),
        updatedTournament.getParticipationType(),
        updatedTournament.getTeamSize(),
        updatedTournament.getTournamentTagsIds());

    return ResponseEntity.ok(tournamentUpdated);
  }

  @GetMapping("/get-all-tournaments")
  @ResponseBody
  public ResponseEntity<?> getAllTournaments() {
    try {
      List<Tournament> tournaments = tournamentService.getAll();
      return ResponseEntity.ok(tournaments);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/get-tournament/{tournamentId}")
  @ResponseBody
  public ResponseEntity<?> getTournament(@PathVariable UUID tournamentId) {
    try {
      Tournament tournament = tournamentService.getTournamentById(tournamentId);
      return ResponseEntity.ok(tournament);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/{userId}/tournaments")
  public ResponseEntity<?> getTournamentsByUserId(@PathVariable String userId) {
    try {
      String decodedUserId = URLDecoder.decode(userId, StandardCharsets.UTF_8);
      List<Tournament> tournaments = tournamentUserTeamService.getTournamentsByUserId(decodedUserId);
      return ResponseEntity.ok(tournaments);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/{tournamentId}/players")
  public ResponseEntity<List<UserDto>> getTournamentPlayers(@PathVariable UUID tournamentId) {
    List<UserDto> participants = tournamentUserTeamService.getPlayersByTournamentId(tournamentId);
    return ResponseEntity.ok(participants);
  }

  @GetMapping("/{tournamentId}/teams")
  public ResponseEntity<List<TeamDto>> getTournamentTeams(@PathVariable UUID tournamentId) {
    try {
      List<TeamDto> teams = tournamentUserTeamService.getTeamsByTournamentId(tournamentId);
      return ResponseEntity.ok(teams);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  @GetMapping("/get-tournament-by-city")
  @ResponseBody
  public ResponseEntity<?> getTournamentByCity(@RequestParam("cityId") UUID cityId) {
    try {
      List<Tournament> filteredTournaments = tournamentService.getByCityId(cityId);
      return ResponseEntity.ok(filteredTournaments);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/get-tournament-by-date-range")
  @ResponseBody
  public ResponseEntity<?> getTournamentsByDateRange(
      @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
      @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
    try {
      List<Tournament> filteredTournaments = tournamentService.getTournamentsByDateRange(startDate, endDate);
      return ResponseEntity.ok(filteredTournaments);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/get-tournament-by-tags")
  @ResponseBody
  public ResponseEntity<?> getTournamentsByTags(@RequestParam("tags") List<UUID> tagIds) {
    try {
      List<Tournament> filteredTournaments = tournamentService.getTournamentsByTags(tagIds);
      return ResponseEntity.ok(filteredTournaments);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PutMapping("{tournamentId}/tournamentTags")
  public ResponseEntity<?> updateTournamentTags(@PathVariable UUID tournamentId, @RequestBody List<UUID> tagIds) {
    try {
      Tournament tournament = tournamentService.getTournamentById(tournamentId);
      Set<TournamentTag> tournamentTags = new HashSet<>();
      for (UUID tagId : tagIds) {
        TournamentTag tournamentTag = tournamentTagService.getTagById(tagId);
        tournamentTags.add(tournamentTag);
      }
      tournament.setTournamentTags(tournamentTags);
      tournamentService.saveTournament(tournament);
      return ResponseEntity.ok("Tournament tournamentTags updated successfully!");
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  private boolean isTournamentFull(Tournament tournament) {
    long currentParticipantsCount = tournamentUserTeamService.getParticipantsCountByTournamentId(tournament.getId());
    return currentParticipantsCount >= tournament.getMaxParticipants();
  }

  @PostMapping("/{tournamentId}/register")
  public ResponseEntity<?> registerUserToTournament(@PathVariable("tournamentId") UUID tournamentId,
                                                    @RequestBody Map<String, String> body) {
    try {
      String email = body.get("email");
      User user = userService.getUserByEmail(email);
      if (user == null) {
        return ResponseEntity.badRequest().body("User with email " + email + " does not exist");
      } else {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);

        if (isTournamentFull(tournament)) {
          return ResponseEntity.badRequest().body("The tournament is already full");
        }

        tournamentUserTeamService.addUserToTournament(user, tournament);
      }
      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PostMapping("/{tournamentId}/registerTeam")
  public ResponseEntity<?> registerUserToTournament(@PathVariable("tournamentId") UUID tournamentId,
                                                    @RequestBody TournamentRegisterTeamDto tournamentRegisterTeamDto) {
    try {
      Tournament tournament = tournamentService.getTournamentById(tournamentId);

      if (isTournamentFull(tournament)) {
        return ResponseEntity.badRequest().body("The tournament is already full");
      } else {

        String teamName = tournamentRegisterTeamDto.getTeamName();
        String leaderId = tournamentRegisterTeamDto.getLeaderId();
        User leader = userService.getUserById(leaderId);

        // Creating temporary team for users
        Team temporaryTeam = new Team();
        temporaryTeam.setName(teamName);
        temporaryTeam.setLeader(leader);
        teamService.saveTeam(temporaryTeam);

        // Adding users to team and tournament
        List<String> emails = tournamentRegisterTeamDto.getEmails();
        for (String email : emails) {
          User user = userService.getUserByEmail(email);
          tournamentUserTeamService.addUserTeamToTournament(user, tournament, temporaryTeam);
        }
      }
      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PostMapping("/{tournamentId}/saveMatches")
  public ResponseEntity<?> saveMatches(@PathVariable("tournamentId") UUID tournamentId,
                                       @RequestBody List<MatchDto> matchDtos) {
    try {
      Tournament tournament = tournamentService.getTournamentById(tournamentId);
      if (tournament.getAreMatchesGenerated()) {
        return ResponseEntity.badRequest().body("Matches already generated for this tournament!");
      }
      tournament.setAreMatchesGenerated(true);

      for (MatchDto matchDto : matchDtos) {
        Match match = matchService.mapMatch(matchDto, tournament);
        matchService.saveMatch(match);

        List<MatchPlayer> matchPlayers = matchPlayerService.mapMatchPlayers(matchDto.getParticipants(), match);
        matchPlayerService.saveMatchPlayers(matchPlayers);

        match.setParticipants(matchPlayers);
      }

      return ResponseEntity.ok("Matches saved successfully!");
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/{tournamentId}/matches")
  @ResponseBody
  public ResponseEntity<?> getMatchesByTournament(@PathVariable("tournamentId") UUID tournamentId) {
    try {
      Tournament tournament = tournamentService.getTournamentById(tournamentId);
      if (!tournament.getAreMatchesGenerated()) {
        return ResponseEntity.badRequest().body("Matches not generated for this tournament!");
      }
      List<Match> matches = matchService.getMatchesByTournament(tournament);
      return ResponseEntity.ok(matches);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/{tournamentId}/participants-count")
  public ResponseEntity<?> getParticipantsCount(@PathVariable UUID tournamentId) {
    try {
      Tournament tournament = tournamentService.getTournamentById(tournamentId);

      if (tournament == null) {
        return ResponseEntity.badRequest().body("Tournament not found with ID: " + tournamentId);
      }

      long participantsCount = tournamentUserTeamService.getParticipantsCountByTournamentId(tournamentId);
      return ResponseEntity.ok(participantsCount);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @DeleteMapping("/{tournamentId}/remove-user/{userId}")
  public ResponseEntity<?> removeUserFromTournament(@PathVariable UUID tournamentId, @PathVariable String userId) {
    try {
      tournamentUserTeamService.removeUserFromTournament(userId, tournamentId);
      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  public Tournament toTournament(TournamentDto tournamentDto) {
    Tournament tournament = new Tournament();

    tournament.setName(tournamentDto.getName());
    tournament.setDescription(tournamentDto.getDescription());
    tournament.setPrizes(tournamentDto.getPrizes());
    tournament.setStartDate(tournamentDto.getStartDate());
    tournament.setEndDate(tournamentDto.getEndDate());
    tournament.setMaxParticipants(tournamentDto.getMaxParticipants());
    tournament.setParticipationType(tournamentDto.getParticipationType());
    tournament.setTeamSize(tournamentDto.getTeamSize());
    tournament.setAreMatchesGenerated(tournamentDto.getAreMatchesGenerated());

    UUID tableTypeId = tournamentDto.getTournamentTableTypeId();
    TournamentTableType tournamentTableType = tournamentTableTypeService.getTournamentTableType(tableTypeId);
    TournamentType tournamentType = tournamentTypeService.getTournamentType(tournamentDto.getTournamentTypeId());
    City city = cityService.getCity(tournamentDto.getCityId());

    tournament.setTournamentTableType(tournamentTableType);
    tournament.setTournamentType(tournamentType);
    tournament.setCity(city);

    if (tournamentDto.getTagIds() != null) {
      Set<TournamentTag> tournamentTags = new HashSet<>();
      for (UUID tagId : tournamentDto.getTagIds()) {
        TournamentTag tournamentTag = tournamentTagService.getTagById(tagId);
        tournamentTags.add(tournamentTag);
      }
      tournament.setTournamentTags(tournamentTags);
    }
    return tournament;
  }
}
