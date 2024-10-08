package com.sourcery.sport.tournament.controller;

import com.sourcery.sport.tournament.model.TournamentType;
import com.sourcery.sport.tournament.service.TournamentTypeService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournament-types")
public class TournamentTypeController {
  private final TournamentTypeService tournamentTypeService;

  public TournamentTypeController(TournamentTypeService tournamentTypeService) {
    this.tournamentTypeService = tournamentTypeService;
  }

  @GetMapping("/get-all-tournament-types")
  @ResponseBody
  public ResponseEntity<?> getAllTableTypes() {
    try {
      List<TournamentType> types = tournamentTypeService.getAll();
      return ResponseEntity.ok(types);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
