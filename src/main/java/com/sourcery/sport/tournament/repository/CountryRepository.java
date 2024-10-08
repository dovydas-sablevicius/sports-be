package com.sourcery.sport.tournament.repository;

import com.sourcery.sport.tournament.model.Country;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, UUID> {

}
