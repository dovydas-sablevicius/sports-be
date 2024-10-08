package com.sourcery.sport.tournament.repository;

import com.sourcery.sport.tournament.model.Tournament;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TournamentRepository extends JpaRepository<Tournament, UUID> {

  @Query("SELECT t FROM Tournament t WHERE t.city.id = :cityId")
  List<Tournament> findTournamentByCityId(@Param("cityId") UUID cityId);

  List<Tournament> findByStartDateBetweenOrEndDateBetween(LocalDateTime startRangeBegin, LocalDateTime startRangeEnd,
                                                          LocalDateTime endRangeBegin, LocalDateTime endRangeEnd);

  List<Tournament> findDistinctByTournamentTags_IdIn(List<UUID> tagIds);
}

