package com.sourcery.sport.tournament.service;

import com.sourcery.sport.tournament.model.City;
import com.sourcery.sport.tournament.repository.CityRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {

  final CityRepository cityRepository;

  public CityServiceImpl(CityRepository cityRepository) {
    this.cityRepository = cityRepository;
  }

  @Override
  public City saveCity(City city) {
    return cityRepository.save(city);
  }

  @Override
  public List<City> getAll() {
    return cityRepository.findAll();
  }

  @Override
  public List<City> getCitiesByCountryId(UUID countryId) {
    return cityRepository.findCitiesByCountry_Id(countryId);
  }

  @Override
  public City getCity(UUID id) {
    return cityRepository.getReferenceById(id);
  }
}
