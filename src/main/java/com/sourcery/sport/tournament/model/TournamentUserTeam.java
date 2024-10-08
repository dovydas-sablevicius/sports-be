package com.sourcery.sport.tournament.model;

import com.sourcery.sport.team.model.Team;
import com.sourcery.sport.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tournament_user_team")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TournamentUserTeam {
  @EmbeddedId
  private TournamentUserTeamId id;

  @ManyToOne
  @MapsId("userId")
  private User user;

  @ManyToOne
  @MapsId("tournamentId")
  private Tournament tournament;

  @ManyToOne(optional = true)
  @JoinColumn(name = "team_id", nullable = true)
  private Team team;

  @Embeddable
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode
  public static class TournamentUserTeamId implements Serializable {
    @Column(name = "user_id")
    private String userId;

    @Column(name = "tournament_id")
    private UUID tournamentId;
  }
}
