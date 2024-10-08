package com.sourcery.sport.tournamenttag.repository;

import com.sourcery.sport.tournamenttag.model.TournamentTag;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentTagRepository extends JpaRepository<TournamentTag, UUID> {

  TournamentTag findTournamentTagById(UUID id);

}
