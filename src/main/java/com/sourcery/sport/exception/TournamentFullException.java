package com.sourcery.sport.exception;

public class TournamentFullException extends RuntimeException {
  public TournamentFullException(String message) {
    super(message);
  }
}
