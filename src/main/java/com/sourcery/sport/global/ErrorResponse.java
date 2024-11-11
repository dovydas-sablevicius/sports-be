package com.sourcery.sport.global;

import java.time.LocalDateTime;


public class ErrorResponse {

  private final LocalDateTime timestamp;
  private final String message;
  private final String details;

  public ErrorResponse(String message, String details) {
    this.message = message;
    this.details = details;
    this.timestamp = LocalDateTime.now();
  }

}
