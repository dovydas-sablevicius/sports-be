package com.sourcery.sport.match.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NotEmpty
public class MatchDto {

  @NotNull(message = "Match ID cannot be null")
  @Size(min = 1, message = "Match ID must not be empty")
  private String id;

  private String nextMatchId;

  @NotEmpty(message = "Tournament round text must not be empty")
  private String tournamentRoundText;

  private LocalDate startTime;

  @NotEmpty(message = "State must not be empty")
  private String state;

  @NotNull(message = "isUpdated cannot be null")
  private Boolean isUpdated;

  @NotNull(message = "Participants cannot be null")
  @Size(min = 1, message = "Match must have at least one participant")
  private List<MatchPlayerDto> participants;

  @NotNull(message = "Match number cannot be null")
  private Integer matchNumber;
}
