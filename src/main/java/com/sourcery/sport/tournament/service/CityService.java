package com.sourcery.sport.tournament.service;

import com.sourcery.sport.tournament.model.City;
import java.util.List;
import java.util.UUID;

public interface CityService {
  City saveCity(City city);

  List<City> getAll();

  List<City> getCitiesByCountryId(UUID countryId);

  City getCity(UUID id);
}
