package com.sourcery.sport.chat.controller;

import com.sourcery.sport.chat.dto.MessageDto;
import com.sourcery.sport.chat.model.Message;
import com.sourcery.sport.chat.service.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @MessageMapping("/chat/{tournamentId}")
  @SendTo("/topic/messages/{tournamentId}")
  public void sendMessage(@DestinationVariable String tournamentId, MessageDto messageDto) {
    messageService.sendMessage(tournamentId, messageDto);
  }

  @GetMapping("/chat/{tournamentId}/messages")
  public Iterable<Message> getMessages(@PathVariable("tournamentId") String tournamentId) {
    return messageService.getListMessages(tournamentId);
  }
}
