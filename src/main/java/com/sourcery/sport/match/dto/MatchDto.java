package com.sourcery.sport.match.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchDto {
  private String id;
  private String nextMatchId;
  private String tournamentRoundText;
  private String startTime;
  private String state;
  private Boolean isUpdated;
  private List<MatchPlayerDto> participants;
  private Integer matchNumber;
}
