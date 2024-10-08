package com.sourcery.sport.match.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sourcery.sport.team.model.Team;
import com.sourcery.sport.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "match_player")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchPlayer {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "match_id", referencedColumnName = "match_id")
  private Match match;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;

  @Column(name = "score")
  private Integer score;

  @Column(name = "is_winner")
  private Boolean isWinner;

  @Column(name = "status")
  private String status;

  @Column(name = "result_text")
  private String resultText;
}
