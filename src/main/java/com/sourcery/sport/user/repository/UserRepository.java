package com.sourcery.sport.user.repository;

import com.sourcery.sport.user.model.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  User findUserById(String id);

  User findUserByEmail(String email);

  @Query("SELECT u.id FROM User u WHERE u.email = ?1")
  UUID findIdByEmail(String email);

  @Query("SELECT u.id FROM User u WHERE u.email IN ?1")
  List<UUID> findIdsByEmails(List<String> emails);

  @Query("SELECT u FROM User u WHERE u.name LIKE :term% OR u.email LIKE :term%")
  List<User> findUsersBySearchTerm(@Param("term") String term);

  @Query(value = "SELECT COUNT(*) FROM tournament_user_team WHERE user_id = ?1 AND tournament_id IN"
      + "(SELECT id from Tournament where end_date <= NOW())", nativeQuery = true)
  Integer getParticipatedTournamentsCount(String userId);

  @Query(value = "SELECT COUNT(*) FROM tournament_user_team WHERE user_id = ?1 AND tournament_id IN"
      + " (SELECT id FROM Tournament WHERE start_date <= NOW()"
      + " AND (end_date >= NOW()))", nativeQuery = true)
  Integer getParticipatingTournamentsCount(String userId);

  @Query(value =
      "SELECT COUNT(DISTINCT tm.tournament_id) "
          + "FROM match_player mp "
          + "JOIN tournament_match tm ON mp.match_id = tm.match_id "
          + "JOIN tournament t ON t.id = tm.tournament_id "
          + "WHERE mp.user_id = ?1 AND mp.is_winner = true AND tm.next_match_id IS NULL AND t.end_date < NOW()",
      nativeQuery = true)
  Integer getWonTournamentsCount(String userId);

  @Query(value = "SELECT tt.id, COUNT(*) AS participation_count "
                  + " FROM tournament_user_team tut "
                  + " JOIN tournament t ON tut.tournament_id = t.id "
                  + " JOIN tournament_type tt ON t.tournament_type_id = tt.id "
                  + " WHERE tut.user_id = ?1 "
                  + " GROUP BY tt.id "
                  + " ORDER BY participation_count DESC "
                  + " LIMIT 1", nativeQuery = true)
  UUID getFavoriteTournamentType(String userId);

  //TODO: update this query or consider adding new when the classic table will be implemented to sum all won tournaments
}
