package com.techres.techresfacebook.service.jbot.facebook;


import com.techres.techresfacebook.domain.Client;
import com.techres.techresfacebook.service.jbot.facebook.models.Message;
import com.techres.techresfacebook.service.jbot.facebook.models.User;
import com.techres.techresfacebook.service.jbot.facebook.models.consume.CollectionComment;
import com.techres.techresfacebook.service.jbot.facebook.models.consume.CollectionFeed;
import com.techres.techresfacebook.webrest.payload.ChatMessageReqDTO;
import io.vavr.control.Try;

public interface FacebookService {

    User getUser(String credId);

    Client getClientByCredId(String facebookId);

    Try<Boolean> registerClientToSystem(String credId, String displayName);

    Boolean sendMessageToUser(String userId, Message message);

    Try<Boolean> saveMessage(String message, Client client);

    Boolean sendText(String userId, ChatMessageReqDTO dto);

    Try<CollectionFeed> fetchCollectionFeed();

    Try<CollectionComment> fetchCollectionCommentByFeed(String feedId);

}

