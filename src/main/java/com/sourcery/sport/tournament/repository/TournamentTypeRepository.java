package com.sourcery.sport.tournament.repository;

import com.sourcery.sport.tournament.model.TournamentType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentTypeRepository extends JpaRepository<TournamentType, UUID> {

}
