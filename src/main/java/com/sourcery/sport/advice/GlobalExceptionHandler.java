package com.sourcery.sport.advice;

import com.sourcery.sport.exception.MatchesAlreadyGeneratedException;
import com.sourcery.sport.exception.MatchesNotGeneratedException;
import com.sourcery.sport.exception.TournamentFullException;
import com.sourcery.sport.exception.TournamentNotFoundException;
import com.sourcery.sport.exception.UserEmailNotFoundException;
import com.sourcery.sport.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(TournamentNotFoundException.class)
  public ResponseEntity<String> handleTournamentNotFound(TournamentNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(MatchesNotGeneratedException.class)
  public ResponseEntity<String> handleMatchesNotGenerated(MatchesNotGeneratedException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(MatchesAlreadyGeneratedException.class)
  public ResponseEntity<String> handleMatchesAlreadyGenerated(MatchesAlreadyGeneratedException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(UserEmailNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> handleUserEmailNotFound(UserEmailNotFoundException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(TournamentFullException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> handleTournamentFull(TournamentFullException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public String handleIllegalArgumentException(IllegalArgumentException ex) {
    return "Invalid input: " + ex.getMessage();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public String handleGeneralException(Exception ex) {
    return "An unexpected error occurred: " + ex.getMessage();
  }
}

