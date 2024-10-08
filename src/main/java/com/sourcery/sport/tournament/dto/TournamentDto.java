package com.sourcery.sport.tournament.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TournamentDto {

  private String name;

  private String description;

  private String prizes;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  private Integer maxParticipants;

  private UUID tournamentTableTypeId;

  private UUID tournamentTypeId;

  private UUID cityId;

  private String participationType;

  private Integer teamSize;

  private List<UUID> tagIds;

  private Boolean areMatchesGenerated;
}
