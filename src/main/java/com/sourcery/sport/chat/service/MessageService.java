package com.sourcery.sport.chat.service;

import com.sourcery.sport.chat.dto.MessageDto;
import com.sourcery.sport.chat.model.Message;
import java.util.List;

public interface MessageService {
  void sendMessage(String to, MessageDto messageDto);

  List<Message> getListMessages(String tournamentId);
}
