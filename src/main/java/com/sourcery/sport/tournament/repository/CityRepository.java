package com.sourcery.sport.tournament.repository;

import com.sourcery.sport.tournament.model.City;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, UUID> {
  List<City> findCitiesByCountry_Id(UUID countryId);
}
