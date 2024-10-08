package com.sourcery.sport.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sourcery.sport.team.model.Team;
import com.sourcery.sport.tournament.model.City;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sports_user")
@Getter
@Setter
@AllArgsConstructor
public class User {

  @Id
  @Column(name = "id", length = 128)
  private String id;

  @Column(name = "name", length = 100)
  private String name;

  @Column(name = "surname", length = 100)
  private String surname;

  @Column(name = "email", length = 100)
  private String email;

  @Column(name = "phone_number", length = 20)
  private String phoneNumber;

  @Column(name = "image")
  private String image;

  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

  @JsonIgnore
  @ManyToMany(mappedBy = "users")
  private Set<Team> teams = new HashSet<>();

  public User() {
  }
}
