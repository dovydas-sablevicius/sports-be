package com.sourcery.sport.tournament.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sourcery.sport.match.model.Match;
import com.sourcery.sport.tournamenttag.model.TournamentTag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tournament")
@Getter
@Setter
public class Tournament {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(length = 32)
  private String name;

  @Column(length = 1024)
  private String description;

  @Column(length = 200)
  private String prizes;

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  @Column(name = "max_participants")
  private Integer maxParticipants;

  @ManyToOne
  @JoinColumn(name = "tournament_table_type_id")
  private TournamentTableType tournamentTableType;

  @ManyToOne
  @JoinColumn(name = "tournament_type_id")
  private TournamentType tournamentType;

  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

  @Column(name = "participation_type", length = 50)
  private String participationType;

  @Column(name = "team_size")
  private Integer teamSize;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "tournament_tag",
      joinColumns = @JoinColumn(name = "tournament_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<TournamentTag> tournamentTags;

  @JsonIgnore
  @OneToMany
  @JoinTable(name = "tournament_match",
      joinColumns = @JoinColumn(name = "tournament_id"),
      inverseJoinColumns = @JoinColumn(name = "match_id"))
  private List<Match> matches;

  @Column(name = "are_matches_generated")
  private Boolean areMatchesGenerated;

  public List<UUID> getTournamentTagsIds() {
    return tournamentTags.stream()
        .map(TournamentTag::getId)
        .collect(Collectors.toList());
  }
}
