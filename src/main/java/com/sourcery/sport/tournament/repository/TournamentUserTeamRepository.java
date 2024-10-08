package com.sourcery.sport.tournament.repository;

import com.sourcery.sport.tournament.model.TournamentUserTeam;
import com.sourcery.sport.tournament.model.TournamentUserTeam.TournamentUserTeamId;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TournamentUserTeamRepository extends JpaRepository<TournamentUserTeam, TournamentUserTeamId> {

  List<TournamentUserTeam> findByUserId(String userId);

  List<TournamentUserTeam> findByTournamentId(UUID tournamentId);

  long countByTournamentId(UUID tournamentId);

  @Modifying
  @Transactional
  @Query("DELETE FROM TournamentUserTeam tut WHERE tut.id.userId = :userId "
      + "AND tut.id.tournamentId = :tournamentId AND tut.team IS NULL")
  void deleteByTournamentIdAndUserId(@Param("userId") String userId, @Param("tournamentId") UUID tournamentId);

  @Modifying
  @Transactional
  @Query("delete from TournamentUserTeam tut where tut.tournament.id = :tournamentId and tut.team.id = :teamId")
  void deleteByTournamentIdAndTeamId(@Param("tournamentId") UUID tournamentId, @Param("teamId") UUID teamId);
}
