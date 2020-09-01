package com.techres.techresfacebook.service.chat;

import com.techres.techresfacebook.domain.Chat;
import com.techres.techresfacebook.repository.ChatRepository;
import com.techres.techresfacebook.repository.ConversationRepository;
import com.techres.techresfacebook.repository.ParticipantRepository;
import com.techres.techresfacebook.repository.StaffRepository;
import com.techres.techresfacebook.service.jbot.facebook.FacebookService;
import com.techres.techresfacebook.service.twilio.TwilioClientService;
import com.techres.techresfacebook.service.twilio.data.ChatPushReqDTO;
import com.techres.techresfacebook.webrest.payload.ChatMessageReqDTO;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    // Spring Data JPA
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private FacebookService facebookService;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private TwilioClientService twilioClientService;



    @Override
    public List<Chat> fetchChatByConversation(Long conversationId) {
        return chatRepository.findAllByConversationId(conversationId);
    }

    @Override
    public Try<Boolean> sendMessageToFacebook(ChatMessageReqDTO dto) {
        val conversation = conversationRepository.findById(dto.getConversationId());
        if (!conversation.isPresent()){
            return Try.failure(new Exception("Failure when fetch conversation by this id:" + dto.getConversationId()));
        }

        val staffUser = staffRepository.findById(dto.getStaffId());
        if (!staffUser.isPresent()){
            return Try.failure(new Exception("Staff not exist with id: " + dto.getStaffId()));
        }

        val client = conversation.get().getClient();
        if (client == null){
            return Try.failure(new Exception("This conversation not mapping with any client "));
        }

        Boolean resultSendMessage = facebookService.sendText(client.getCredentialPlatform(), dto);
        if (resultSendMessage){

            val chat = new Chat();
            chat.setDateCreated(Instant.now());
            chat.setContent(dto.getContent());
            chat.setSender(staffUser.get().getUser());
            chat.setConversation(conversation.get());
            chatRepository.save(chat);

            val chatPush = new ChatPushReqDTO();
            chatPush.setContent(dto.getContent());
            chatPush.setFromSender(staffUser.get().getUser().getDisplayName());
            chatPush.setUniqueIdChannel("CHATBOX_"+conversation.get().getId());
            return Try.of(() -> twilioClientService.pushChat(chatPush).get());
        } else {
            return Try.failure(new Exception("Failure when send message to facebook"));
        }

    }
}
