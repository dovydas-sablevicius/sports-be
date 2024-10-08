package com.sourcery.sport.tournament.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TournamentCreatedDto {

  private UUID id;

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
}
