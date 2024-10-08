package com.sourcery.sport.chat.repository;

import com.sourcery.sport.chat.model.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
  List<Message> findByTournamentIdOrderByTimestamp(UUID tournamentId);
}
