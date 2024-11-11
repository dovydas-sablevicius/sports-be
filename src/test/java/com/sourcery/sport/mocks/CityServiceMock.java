package com.sourcery.sport.mocks;

import com.sourcery.sport.tournament.model.City;
import com.sourcery.sport.tournament.service.CityService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CityServiceMock implements CityService {
  private final Map<UUID, City> cityStore = new HashMap<>();

  @Override
  public City saveCity(City city) {
    cityStore.put(city.getId(), city);
    return city;
  }

  @Override
  public List<City> getAll() {
    return new ArrayList<>(cityStore.values());
  }

  @Override
  public List<City> getCitiesByCountryId(UUID countryId) {
    List<City> cities = new ArrayList<>();
    for (City city : cityStore.values()) {
      if (city.getCountry() != null && countryId.equals(city.getCountry().getId())) {
        cities.add(city);
      }
    }
    return cities;
  }

  @Override
  public City getCity(UUID id)  {
    return cityStore.get(id);
  }
}