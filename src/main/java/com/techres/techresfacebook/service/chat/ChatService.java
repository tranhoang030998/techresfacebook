package com.techres.techresfacebook.service.chat;

import com.techres.techresfacebook.domain.Chat;
import com.techres.techresfacebook.webrest.payload.ChatMessageReqDTO;
import io.vavr.control.Try;

import java.util.List;

public interface ChatService {

    List<Chat> fetchChatByConversation(Long conversationId);

    Try<Boolean> sendMessageToFacebook(ChatMessageReqDTO dto);

}
