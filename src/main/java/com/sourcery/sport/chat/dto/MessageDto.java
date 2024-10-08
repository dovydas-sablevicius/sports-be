package com.sourcery.sport.chat.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
  private String content;
  private String userId;
  private LocalDateTime timestamp;
}
