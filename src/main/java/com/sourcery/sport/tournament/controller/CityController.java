package com.sourcery.sport.tournament.controller;

import com.sourcery.sport.tournament.model.City;
import com.sourcery.sport.tournament.service.CityService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cities")
public class CityController {
  private final CityService cityService;

  public CityController( CityService cityService) {
    this.cityService = cityService;
  }

  @GetMapping("get-all-cities")
  @ResponseBody
  public ResponseEntity<?> getAllCities() {
    try {
      List<City> cities = cityService.getAll();
      return ResponseEntity.ok(cities);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/by-country/{countryId}")
  @ResponseBody
  public ResponseEntity<?> getCitiesByCountryId(@PathVariable UUID countryId) {
    try {
      List<City> cities = cityService.getCitiesByCountryId(countryId);
      return ResponseEntity.ok(cities);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
