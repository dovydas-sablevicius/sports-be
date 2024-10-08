package com.sourcery.sport.match.repository;

import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.tournament.model.Tournament;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchRepository extends JpaRepository<Match, String> {
  @Query("SELECT m FROM Match m WHERE m.tournament = ?1 ORDER BY m.matchNumber ASC")
  List<Match> findByTournament(Tournament tournament);
}
