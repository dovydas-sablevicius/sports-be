package com.sourcery.sport.tournament.controller;

import com.sourcery.sport.tournament.model.TournamentTableType;
import com.sourcery.sport.tournament.service.TournamentTableTypeService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/table-types")
public class TournamentTableTypeController {
  private final TournamentTableTypeService tournamentTableTypeService;

  public TournamentTableTypeController(TournamentTableTypeService tournamentTableTypeService) {
    this.tournamentTableTypeService = tournamentTableTypeService;
  }

  @GetMapping("/get-all-table-types")
  @ResponseBody
  public ResponseEntity<?> getAllTableTypes() {
    try {
      List<TournamentTableType> types = tournamentTableTypeService.getAll();
      return ResponseEntity.ok(types);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
