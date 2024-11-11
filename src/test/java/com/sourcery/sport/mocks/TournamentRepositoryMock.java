package com.sourcery.sport.mocks;

import com.sourcery.sport.tournament.model.Tournament;
import com.sourcery.sport.tournament.repository.TournamentRepository;
import com.sourcery.sport.tournamenttag.model.TournamentTag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class TournamentRepositoryMock implements TournamentRepository {
  private final Map<UUID, Tournament> tournamentStore = new HashMap<>();

  @Override
  public Tournament save(Tournament tournament) {
    tournamentStore.put(tournament.getId(), tournament);
    return tournament;
  }

  @Override
  public <S extends Tournament> List<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public List<Tournament> findAll() {
    return new ArrayList<>(tournamentStore.values());
  }

  @Override
  public List<Tournament> findAllById(Iterable<UUID> uuids) {
    return null;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(UUID uuid) {

  }

  @Override
  public void delete(Tournament entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends UUID> uuids) {

  }

  @Override
  public void deleteAll(Iterable<? extends Tournament> entities) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public Optional<Tournament> findById(UUID id) {
    return Optional.ofNullable(tournamentStore.get(id));
  }

  @Override
  public boolean existsById(UUID uuid) {
    return false;
  }

  @Override
  public List<Tournament> findTournamentByCityId(UUID cityId) {
    List<Tournament> tournaments = new ArrayList<>();
    for (Tournament tournament : tournamentStore.values()) {
      if (tournament.getCity() != null && cityId.equals(tournament.getCity().getId())) {
        tournaments.add(tournament);
      }
    }
    return tournaments;
  }

  @Override
  public List<Tournament> findByStartDateBetweenOrEndDateBetween(
      LocalDateTime startRange, LocalDateTime endRange, LocalDateTime startRange2, LocalDateTime endRange2) {
    List<Tournament> tournaments = new ArrayList<>();
    for (Tournament tournament : tournamentStore.values()) {
      if ((tournament.getStartDate().isAfter(startRange) && tournament.getStartDate().isBefore(endRange)) ||
          (tournament.getEndDate().isAfter(startRange2) && tournament.getEndDate().isBefore(endRange2))) {
        tournaments.add(tournament);
      }
    }
    return tournaments;
  }

  @Override
  public List<Tournament> findDistinctByTournamentTags_IdIn(List<UUID> tagsId) {
    List<Tournament> tournaments = new ArrayList<>();
    for (Tournament tournament : tournamentStore.values()) {
      for (TournamentTag tag : tournament.getTournamentTags()) {
        if (tagsId.contains(tag.getId())) {
          tournaments.add(tournament);
          break;
        }
      }
    }
    return tournaments;
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends Tournament> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public <S extends Tournament> List<S> saveAllAndFlush(Iterable<S> entities) {
    return null;
  }

  @Override
  public void deleteAllInBatch(Iterable<Tournament> entities) {

  }

  @Override
  public void deleteAllByIdInBatch(Iterable<UUID> uuids) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public Tournament getOne(UUID uuid) {
    return null;
  }

  @Override
  public Tournament getById(UUID uuid) {
    return null;
  }

  @Override
  public Tournament getReferenceById(UUID uuid) {
    return null;
  }

  @Override
  public <S extends Tournament> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends Tournament> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends Tournament> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public <S extends Tournament> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends Tournament> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends Tournament> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends Tournament, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }

  @Override
  public List<Tournament> findAll(Sort sort) {
    return null;
  }

  @Override
  public Page<Tournament> findAll(Pageable pageable) {
    return null;
  }
}

