package com.sourcery.sport.match.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchPlayerDto {
  private String id;
  private Integer score;
  private Boolean isWinner;
  private String status;
  private String type;
}
