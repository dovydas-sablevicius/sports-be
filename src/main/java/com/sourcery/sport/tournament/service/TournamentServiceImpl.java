package com.sourcery.sport.tournament.service;

import com.sourcery.sport.exception.TournamentNotFoundException;
import com.sourcery.sport.tournament.dto.TournamentUpdateDto;
import com.sourcery.sport.tournament.model.City;
import com.sourcery.sport.tournament.model.Tournament;
import com.sourcery.sport.tournament.model.TournamentTableType;
import com.sourcery.sport.tournament.model.TournamentType;
import com.sourcery.sport.tournament.repository.TournamentRepository;
import com.sourcery.sport.tournamenttag.model.TournamentTag;
import com.sourcery.sport.tournamenttag.service.TournamentTagService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TournamentServiceImpl implements TournamentService {

  private final TournamentRepository tournamentRepository;
  private final TournamentTableTypeService tournamentTableTypeService;
  private final TournamentTypeService tournamentTypeService;
  private final CityService cityService;
  private final TournamentTagService tournamentTagService;

  public TournamentServiceImpl(TournamentRepository tournamentRepository,
                               TournamentTableTypeService tournamentTableTypeService,
                               TournamentTypeService tournamentTypeService,
                               CityService cityService,
                               TournamentTagService tournamentTagService) {
    this.tournamentRepository = tournamentRepository;
    this.tournamentTableTypeService = tournamentTableTypeService;
    this.tournamentTypeService = tournamentTypeService;
    this.cityService = cityService;
    this.tournamentTagService = tournamentTagService;
  }

  @Override
  public Tournament saveTournament(Tournament tournament) {
    return tournamentRepository.save(tournament);
  }

  @Override
  public List<Tournament> getAll() {
    return tournamentRepository.findAll();
  }

  @Override
  public List<Tournament> getByCityId(UUID cityId) {
    return tournamentRepository.findTournamentByCityId(cityId);
  }

  @Override
  public Tournament getTournamentById(UUID id) {
    return tournamentRepository.findById(id)
        .orElseThrow(() -> new TournamentNotFoundException("Tournament not found with ID: " + id));
  }


  @Override
  public List<Tournament> getTournamentsByDateRange(LocalDateTime startRange, LocalDateTime endRange) {
    return tournamentRepository.findByStartDateBetweenOrEndDateBetween(startRange, endRange, startRange, endRange);
  }

  @Override
  public List<Tournament> getTournamentsByTags(List<UUID> tagsId) {
    return tournamentRepository.findDistinctByTournamentTags_IdIn(tagsId);
  }

  @Override
  public Tournament updateTournament(Tournament existingTournament, TournamentUpdateDto tournamentDto) {
    existingTournament.setName(tournamentDto.getName());
    existingTournament.setDescription(tournamentDto.getDescription());
    existingTournament.setPrizes(tournamentDto.getPrizes());
    existingTournament.setStartDate(tournamentDto.getStartDate());
    existingTournament.setEndDate(tournamentDto.getEndDate());
    existingTournament.setMaxParticipants(tournamentDto.getMaxParticipants());

    TournamentTableType tournamentTableType = tournamentTableTypeService
        .getTournamentTableType(tournamentDto.getTournamentTableTypeId());
    TournamentType tournamentType = tournamentTypeService.getTournamentType(tournamentDto.getTournamentTypeId());
    City city = cityService.getCity(tournamentDto.getCityId());

    existingTournament.setTournamentTableType(tournamentTableType);
    existingTournament.setTournamentType(tournamentType);
    existingTournament.setCity(city);

    existingTournament.setParticipationType(tournamentDto.getParticipationType());
    existingTournament.setTeamSize(tournamentDto.getTeamSize());

    List<TournamentTag> tournamentTags = tournamentDto.getTagIds().stream()
        .map(tournamentTagService::getTagById).toList();

    existingTournament.setTournamentTags(new HashSet<>(tournamentTags));

    return saveTournament(existingTournament);
  }
}
