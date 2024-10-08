package com.sourcery.sport.chat.service;

import com.sourcery.sport.chat.dto.MessageDto;
import com.sourcery.sport.chat.model.Message;
import com.sourcery.sport.chat.repository.MessageRepository;
import com.sourcery.sport.tournament.service.TournamentService;
import com.sourcery.sport.user.service.UserService;
import java.util.List;
import java.util.UUID;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

  private final SimpMessagingTemplate simpMessagingTemplate;

  private final MessageRepository messageRepository;

  private final TournamentService tournamentService;

  private final UserService userService;

  public MessageServiceImpl(SimpMessagingTemplate simpMessagingTemplate, MessageRepository messageRepository,
                            TournamentService tournamentService, UserService userService) {
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.messageRepository = messageRepository;
    this.tournamentService = tournamentService;
    this.userService = userService;
  }

  @Override
  public void sendMessage(String tournamentId, MessageDto messageDto) {
    Message message = new Message();
    message.setTournament(tournamentService.getTournamentById(UUID.fromString(tournamentId)));
    message.setId(UUID.randomUUID());
    message.setTimestamp(messageDto.getTimestamp());
    message.setContent(messageDto.getContent());
    message.setUser(userService.getUserById(messageDto.getUserId()));
    messageRepository.save(message);
    simpMessagingTemplate.convertAndSend("/topic/messages/" + tournamentId, message);
  }

  @Override
  public List<Message> getListMessages(String tournamentId) {
    UUID tournamentUUID = UUID.fromString(tournamentId);
    return messageRepository.findByTournamentIdOrderByTimestamp(tournamentUUID);
  }
}
