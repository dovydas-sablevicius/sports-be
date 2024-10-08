package com.sourcery.sport.tournament.service;

import com.sourcery.sport.tournament.model.Country;
import java.util.List;

public interface CountryService {

  Country saveCountry(Country country);

  List<Country> getAll();

}
