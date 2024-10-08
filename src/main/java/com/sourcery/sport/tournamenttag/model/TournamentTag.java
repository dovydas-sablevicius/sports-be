package com.sourcery.sport.tournamenttag.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sourcery.sport.tournament.model.Tournament;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tag")
@Getter
@Setter
public class TournamentTag {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private UUID id;

  @Column(name = "name", length = 32)
  @NotBlank(message = "Name is mandatory")
  private String name;

  @JsonIgnore
  @ManyToMany(mappedBy = "tournamentTags")
  private Set<Tournament> tagged;
}
