package com.sourcery.sport.exception;

public class TournamentNotFoundException extends RuntimeException{
  public TournamentNotFoundException(String message) {
    super(message);
  }
}
