package com.sourcery.sport.mocks;

import com.sourcery.sport.exception.TournamentNotFoundException;
import com.sourcery.sport.tournament.dto.TournamentUpdateDto;
import com.sourcery.sport.tournament.model.City;
import com.sourcery.sport.tournament.model.Tournament;
import com.sourcery.sport.tournament.model.TournamentTableType;
import com.sourcery.sport.tournament.model.TournamentType;
import com.sourcery.sport.tournament.service.TournamentService;
import com.sourcery.sport.tournamenttag.model.TournamentTag;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TournamentServiceMock implements TournamentService {
  private final Map<UUID, Tournament> tournamentStore = new HashMap<>();
  private final TournamentTableTypeServiceMock tournamentTableTypeService;
  private final TournamentTypeServiceMock tournamentTypeService;
  private final CityServiceMock cityService;
  private final TournamentTagServiceMock tournamentTagService;

  public TournamentServiceMock(
      TournamentTableTypeServiceMock tournamentTableTypeService,
      TournamentTypeServiceMock tournamentTypeService,
      CityServiceMock cityService,
      TournamentTagServiceMock tournamentTagService
  ) {
    this.tournamentTableTypeService = tournamentTableTypeService;
    this.tournamentTypeService = tournamentTypeService;
    this.cityService = cityService;
    this.tournamentTagService = tournamentTagService;
  }

  @Override
  public Tournament saveTournament(Tournament tournament) {
    tournamentStore.put(tournament.getId(), tournament);
    return tournament;
  }

  @Override
  public List<Tournament> getAll() {
    return new ArrayList<>(tournamentStore.values());
  }

  @Override
  public List<Tournament> getByCityId(UUID cityId) {
    return tournamentStore.values().stream()
        .filter(tournament -> tournament.getCity() != null && cityId.equals(tournament.getCity().getId()))
        .collect(Collectors.toList());
  }

  @Override
  public Tournament getTournamentById(UUID id) {
    Tournament tournament = tournamentStore.get(id);
    if (tournament == null) {
      throw new TournamentNotFoundException("Tournament not found with ID: " + id);
    }
    return tournament;
  }

  @Override
  public List<Tournament> getTournamentsByDateRange(LocalDateTime startRange, LocalDateTime endRange) {
    return tournamentStore.values().stream()
        .filter(tournament ->
            (tournament.getStartDate().isAfter(startRange) && tournament.getStartDate().isBefore(endRange)) ||
                (tournament.getEndDate().isAfter(startRange) && tournament.getEndDate().isBefore(endRange))
        )
        .collect(Collectors.toList());
  }

  @Override
  public List<Tournament> getTournamentsByTags(List<UUID> tagsId) {
    return tournamentStore.values().stream()
        .filter(tournament -> tournament.getTournamentTags().stream()
            .anyMatch(tag -> tagsId.contains(tag.getId())))
        .collect(Collectors.toList());
  }

  @Override
  public Tournament updateTournament(Tournament existingTournament, TournamentUpdateDto tournamentDto) {
    existingTournament.setName(tournamentDto.getName());
    existingTournament.setDescription(tournamentDto.getDescription());
    existingTournament.setPrizes(tournamentDto.getPrizes());
    existingTournament.setStartDate(tournamentDto.getStartDate());
    existingTournament.setEndDate(tournamentDto.getEndDate());
    existingTournament.setMaxParticipants(tournamentDto.getMaxParticipants());

    TournamentTableType tournamentTableType = tournamentTableTypeService.getTournamentTableType(tournamentDto.getTournamentTableTypeId());
    TournamentType tournamentType = tournamentTypeService.getTournamentType(tournamentDto.getTournamentTypeId());
    City city = cityService.getCity(tournamentDto.getCityId());

    existingTournament.setTournamentTableType(tournamentTableType);
    existingTournament.setTournamentType(tournamentType);
    existingTournament.setCity(city);

    existingTournament.setParticipationType(tournamentDto.getParticipationType());
    existingTournament.setTeamSize(tournamentDto.getTeamSize());

    List<TournamentTag> tournamentTags = tournamentDto.getTagIds().stream()
        .map(tournamentTagService::getTagById)
        .toList();
    existingTournament.setTournamentTags(new HashSet<>(tournamentTags));

    return saveTournament(existingTournament);
  }
}

