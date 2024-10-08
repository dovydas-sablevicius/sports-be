package com.sourcery.sport.tournament.repository;

import com.sourcery.sport.tournament.model.TournamentTableType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentTableTypeRepository extends JpaRepository<TournamentTableType, UUID> {

}
