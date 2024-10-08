package com.sourcery.sport.tournamenttag.controller;

import com.sourcery.sport.tournamenttag.model.TournamentTag;
import com.sourcery.sport.tournamenttag.service.TournamentTagService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournamentTags")
public class TournamentTagController {

  private final TournamentTagService tournamentTagService;

  public TournamentTagController(TournamentTagService tournamentTagService) {
    this.tournamentTagService = tournamentTagService;
  }

  @PostMapping("/create-tag")
  public ResponseEntity<?> createTag(@RequestBody TournamentTag tournamentTag) {
    try {
      tournamentTagService.saveTag(tournamentTag);
      return ResponseEntity.ok("Tag created successfully!");
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/get-all-tags")
  public ResponseEntity<?> getAllTags() {
    try {
      List<TournamentTag> tournamentTags = tournamentTagService.getAllTags();
      return ResponseEntity.ok(tournamentTags);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
