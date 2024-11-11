package com.sourcery.sport.tournament.controller;

import com.sourcery.sport.exception.MatchesNotGeneratedException;
import com.sourcery.sport.exception.TournamentFullException;
import com.sourcery.sport.exception.MatchesAlreadyGeneratedException;
import com.sourcery.sport.exception.UserEmailNotFoundException;
import com.sourcery.sport.match.dto.MatchDto;
import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.match.model.MatchPlayer;
import com.sourcery.sport.match.service.MatchPlayerService;
import com.sourcery.sport.match.service.MatchService;
import com.sourcery.sport.team.dto.TeamDto;
import com.sourcery.sport.team.model.Team;
import com.sourcery.sport.team.service.TeamService;
import com.sourcery.sport.tournament.dto.TournamentDto;
import com.sourcery.sport.tournament.dto.TournamentRegisterTeamDto;
import com.sourcery.sport.tournament.dto.TournamentUpdateDto;
import com.sourcery.sport.tournament.mapper.TournamentMapper;
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
  @PostMapping("/create")
  public ResponseEntity<?> createTournament(@RequestBody TournamentDto tournamentDto) {
    validateTournamentDto(tournamentDto);

    Tournament tournament = tournamentService.saveTournament(toTournament(tournamentDto));

    return ResponseEntity.ok(TournamentMapper.mapToTournamentCreatedDto(tournament));
  }

  @PreAuthorize("hasAuthority('admin')")
  @PutMapping("/update")
  public ResponseEntity<?> updateTournament(@RequestBody TournamentUpdateDto tournamentDto) {
    UUID tournamentId = tournamentDto.getId();
    Tournament existingTournament = tournamentService.getTournamentById(tournamentId);

    if (existingTournament == null) {
      return ResponseEntity.notFound().build();
    }

    Tournament updatedTournament = tournamentService.updateTournament(existingTournament, tournamentDto);

    return ResponseEntity.ok(TournamentMapper.mapToTournamentUpdateDto(updatedTournament));
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAllTournaments() {
      List<Tournament> tournaments = tournamentService.getAll();
      return ResponseEntity.ok(tournaments);
  }

  @GetMapping("/get-tournament/{tournamentId}")
  @ResponseBody
  public ResponseEntity<Tournament> getTournament(@PathVariable UUID tournamentId) {
    Tournament tournament = tournamentService.getTournamentById(tournamentId);
    return ResponseEntity.ok(tournament);
  }

  @GetMapping("/{userId}/tournaments")
  public ResponseEntity<List<Tournament>> getTournamentsByUserId(@PathVariable String userId) {
    String decodedUserId = URLDecoder.decode(userId, StandardCharsets.UTF_8);
    List<Tournament> tournaments = tournamentUserTeamService.getTournamentsByUserId(decodedUserId);
    return ResponseEntity.ok(tournaments);
  }

  @GetMapping("/{tournamentId}/players")
  public ResponseEntity<List<UserDto>> getTournamentPlayers(@PathVariable UUID tournamentId) {
    List<UserDto> participants = tournamentUserTeamService.getPlayersByTournamentId(tournamentId);
    return ResponseEntity.ok(participants);
  }

  @GetMapping("/{tournamentId}/teams")
  public ResponseEntity<List<TeamDto>> getTournamentTeams(@PathVariable UUID tournamentId) {
    List<TeamDto> teams = tournamentUserTeamService.getTeamsByTournamentId(tournamentId);
    return ResponseEntity.ok(teams);
  }

  @GetMapping("/get-tournament-by-city")
  @ResponseBody
  public ResponseEntity<List<Tournament>> getTournamentByCity(@RequestParam("cityId") UUID cityId) {
    List<Tournament> filteredTournaments = tournamentService.getByCityId(cityId);
    return ResponseEntity.ok(filteredTournaments);
  }


  @GetMapping("/get-tournament-by-date-range")
  @ResponseBody
  public ResponseEntity<List<Tournament>> getTournamentsByDateRange(
      @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
      @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

    List<Tournament> filteredTournaments = tournamentService.getTournamentsByDateRange(startDate, endDate);
    return ResponseEntity.ok(filteredTournaments);
  }


  @GetMapping("/get-tournament-by-tags")
  @ResponseBody
  public ResponseEntity<List<Tournament>> getTournamentsByTags(@RequestParam("tags") List<UUID> tagIds) {
    List<Tournament> filteredTournaments = tournamentService.getTournamentsByTags(tagIds);
    return ResponseEntity.ok(filteredTournaments);
  }


  @PutMapping("{tournamentId}/tournamentTags")
  public ResponseEntity<String> updateTournamentTags(@PathVariable UUID tournamentId, @RequestBody List<UUID> tagIds) {
    Tournament tournament = tournamentService.getTournamentById(tournamentId);
    Set<TournamentTag> tournamentTags = fetchTournamentTags(tagIds);

    updateTournamentWithTags(tournament, tournamentTags);

    return ResponseEntity.ok("Tournament tags updated successfully!");
  }

  @PostMapping("/{tournamentId}/register")
  public ResponseEntity<Void> registerUserToTournament(
      @PathVariable("tournamentId") UUID tournamentId,
      @RequestBody Map<String, String> body) {

    String email = body.get("email");
    User user = validateUserByEmail(email);

    Tournament tournament = validateTournamentAvailability(tournamentId);

    tournamentUserTeamService.addUserToTournament(user, tournament);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{tournamentId}/registerTeam")
  public ResponseEntity<Void> registerTeamToTournament(
      @PathVariable("tournamentId") UUID tournamentId,
      @RequestBody TournamentRegisterTeamDto tournamentRegisterTeamDto) {

    Tournament tournament = validateTournamentAvailability(tournamentId);

    String teamName = tournamentRegisterTeamDto.getTeamName();
    User leader = userService.getUserById(tournamentRegisterTeamDto.getLeaderId());

    Team temporaryTeam = createTemporaryTeam(teamName, leader);

    List<String> emails = tournamentRegisterTeamDto.getEmails();
    addUsersToTournament(emails, tournament, temporaryTeam);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/{tournamentId}/saveMatches")
  public ResponseEntity<String> saveMatches(
      @PathVariable("tournamentId") UUID tournamentId,
      @RequestBody List<MatchDto> matchDtos) {

    Tournament tournament = checkIfMatchesGenerated(tournamentId);  // Throws TournamentMatchesAlreadyGeneratedException if matches are already generated

    tournament.setAreMatchesGenerated(true);
    saveAllMatchesWithParticipants(matchDtos, tournament);

    return ResponseEntity.ok("Matches saved successfully!");
  }

  @GetMapping("/{tournamentId}/matches")
  @ResponseBody
  public ResponseEntity<List<Match>> getMatchesByTournament(@PathVariable("tournamentId") UUID tournamentId) {
    Tournament tournament = checkIfMatchesNotGenerated(tournamentId);  // Throws MatchesNotGeneratedException if matches are not generated
    List<Match> matches = matchService.getMatchesByTournament(tournament);
    return ResponseEntity.ok(matches);
  }

  @GetMapping("/{tournamentId}/participants-count")
  public ResponseEntity<Long> getParticipantsCount(@PathVariable UUID tournamentId) {
    Tournament tournament = tournamentService.getTournamentById(tournamentId);  // Throws TournamentNotFoundException if not found
    long participantsCount = tournamentUserTeamService.getParticipantsCountByTournamentId(tournamentId);
    return ResponseEntity.ok(participantsCount);
  }


  @DeleteMapping("/{tournamentId}/remove-user/{userId}")
  public ResponseEntity<Void> removeUserFromTournament(@PathVariable UUID tournamentId, @PathVariable String userId) {
    tournamentUserTeamService.removeUserFromTournament(userId, tournamentId);
    return ResponseEntity.ok().build();
  }

  private void validateTournamentDto(TournamentDto tournamentDto) {
    if (tournamentDto.getTournamentTableTypeId() == null ||
        tournamentDto.getTournamentTypeId() == null ||
        tournamentDto.getCityId() == null) {
      throw new IllegalArgumentException("One or more required fields are missing.");
    }
  }

  private Set<TournamentTag> fetchTournamentTags(List<UUID> tagIds) {
    Set<TournamentTag> tournamentTags = new HashSet<>();
    for (UUID tagId : tagIds) {
      TournamentTag tournamentTag = tournamentTagService.getTagById(tagId);
      tournamentTags.add(tournamentTag);
    }
    return tournamentTags;
  }

  private void updateTournamentWithTags(Tournament tournament, Set<TournamentTag> tournamentTags) {
    tournament.setTournamentTags(tournamentTags);
    tournamentService.saveTournament(tournament);
  }

  private boolean isTournamentFull(Tournament tournament) {
    long currentParticipantsCount = tournamentUserTeamService.getParticipantsCountByTournamentId(tournament.getId());
    return currentParticipantsCount >= tournament.getMaxParticipants();
  }

  private User validateUserByEmail(String email) {
    User user = userService.getUserByEmail(email);
    if (user == null) {
      throw new UserEmailNotFoundException(email);
    }
    return user;
  }

  private Tournament validateTournamentAvailability(UUID tournamentId) {
    Tournament tournament = tournamentService.getTournamentById(tournamentId);
    if (isTournamentFull(tournament)) {
      throw new TournamentFullException("The tournament is already full");
    }
    return tournament;
  }


  private Team createTemporaryTeam(String teamName, User leader) {
    Team temporaryTeam = new Team();
    temporaryTeam.setName(teamName);
    temporaryTeam.setLeader(leader);
    teamService.saveTeam(temporaryTeam);
    return temporaryTeam;
  }

  private void addUsersToTournament(List<String> emails, Tournament tournament, Team temporaryTeam) {
    for (String email : emails) {
      User user = validateUserByEmail(email);
      tournamentUserTeamService.addUserTeamToTournament(user, tournament, temporaryTeam);
    }
  }

  private Tournament checkIfMatchesGenerated(UUID tournamentId) {
    Tournament tournament = tournamentService.getTournamentById(tournamentId);
    if (tournament.getAreMatchesGenerated()) {
      throw new MatchesAlreadyGeneratedException("Matches already generated for this tournament!");
    }
    return tournament;
  }

  private void saveAllMatchesWithParticipants(List<MatchDto> matchDtos, Tournament tournament) {
    for (MatchDto matchDto : matchDtos) {
      Match match = matchService.mapMatch(matchDto, tournament);
      matchService.saveMatch(match);

      List<MatchPlayer> matchPlayers = matchPlayerService.mapMatchPlayers(matchDto.getParticipants(), match);
      matchPlayerService.saveMatchPlayers(matchPlayers);

      match.setParticipants(matchPlayers);
    }
  }

  private Tournament checkIfMatchesNotGenerated(UUID tournamentId) {
    Tournament tournament = tournamentService.getTournamentById(tournamentId);
    if (!tournament.getAreMatchesGenerated()) {
      throw new MatchesNotGeneratedException("Matches not generated for this tournament!");
    }
    return tournament;
  }

  private Tournament toTournament(TournamentDto tournamentDto) {
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
