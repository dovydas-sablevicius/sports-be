package com.sourcery.sport.match.repository;

import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.match.model.MatchPlayer;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, UUID> {
  List<MatchPlayer> findByMatch(Match match);
}
