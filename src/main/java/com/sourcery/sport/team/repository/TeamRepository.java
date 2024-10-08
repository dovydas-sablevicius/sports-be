package com.sourcery.sport.team.repository;

import com.sourcery.sport.team.model.Team;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, UUID> {

  Team findByName(String name);
}
