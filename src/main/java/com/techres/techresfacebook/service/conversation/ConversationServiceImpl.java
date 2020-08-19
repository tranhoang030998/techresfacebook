package com.techres.techresfacebook.service.conversation;

import com.techres.techresfacebook.domain.Conversation;
import com.techres.techresfacebook.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public List<Conversation> fetchAllConversation() {
        return conversationRepository.findAll();
    }

}
