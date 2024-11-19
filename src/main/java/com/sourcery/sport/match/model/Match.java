package com.sourcery.sport.match.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sourcery.sport.tournament.model.Tournament;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "tournament_match")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Match {
  @Id
  @Column(name = "match_id")
  private String matchId;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "tournament_id")
  private Tournament tournament;

  @OneToMany(mappedBy = "match")
  private List<MatchPlayer> participants;

  @Column(name = "next_match_id")
  private String nextMatchId;

  @Column(name = "state")
  private String state;

  @Column(name = "round")
  private Integer round;

  @Column(name = "start_time")
  private LocalDate startTime;

  @Column(name = "is_updated")
  private Boolean isUpdated;

  @JsonIgnore
  @Column(name = "match_number")
  private Integer matchNumber;
}
