package com.sourcery.sport.tournament.service;

import com.sourcery.sport.tournament.model.Country;
import com.sourcery.sport.tournament.repository.CountryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CountryServiceImpl implements CountryService {

  final CountryRepository countryRepository;

  public CountryServiceImpl(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
  }

  @Override
  public Country saveCountry(Country country) {
    return countryRepository.save(country);
  }

  @Override
  public List<Country> getAll() {
    return countryRepository.findAll();
  }
}
