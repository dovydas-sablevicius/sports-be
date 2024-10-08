package com.sourcery.sport.chat.model;

import com.sourcery.sport.tournament.model.Tournament;
import com.sourcery.sport.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "message_table")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Message {
  @Id
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "tournament_id")
  private Tournament tournament;

  @Column(name = "content", length = 1000)
  private String content;

  @Column(name = "created_at")
  private LocalDateTime timestamp;

}
