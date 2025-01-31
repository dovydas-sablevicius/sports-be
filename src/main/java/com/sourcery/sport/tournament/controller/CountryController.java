package com.sourcery.sport.tournament.controller;

import com.sourcery.sport.tournament.model.Country;
import com.sourcery.sport.tournament.service.CityService;
import com.sourcery.sport.tournament.service.CountryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
public class CountryController {
  private final CountryService countryService;
  private final CityService cityService;

  public CountryController(CountryService countryService, CityService cityService) {
    this.countryService = countryService;
    this.cityService = cityService;
  }

  @GetMapping("get-all-countries")
  @ResponseBody
  public ResponseEntity<?> getAllCountries() {
    try {
      List<Country> countries = countryService.getAll();
      return ResponseEntity.ok(countries);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
