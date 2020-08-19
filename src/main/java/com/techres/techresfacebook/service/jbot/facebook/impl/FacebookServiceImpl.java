package com.techres.techresfacebook.service.jbot.facebook.impl;


import com.techres.techresfacebook.domain.Chat;
import com.techres.techresfacebook.domain.Client;
import com.techres.techresfacebook.domain.Conversation;
import com.techres.techresfacebook.domain.Participant;
import com.techres.techresfacebook.domain.enumration.Role;
import com.techres.techresfacebook.repository.*;
import com.techres.techresfacebook.service.jbot.common.EventType;
import com.techres.techresfacebook.service.jbot.endpoint.FaceBookApiEndPoints;
import com.techres.techresfacebook.service.jbot.facebook.FacebookService;
import com.techres.techresfacebook.service.jbot.facebook.models.Event;
import com.techres.techresfacebook.service.jbot.facebook.models.Message;
import com.techres.techresfacebook.service.jbot.facebook.models.Response;
import com.techres.techresfacebook.service.jbot.facebook.models.User;
import com.techres.techresfacebook.service.jbot.facebook.models.consume.CollectionComment;
import com.techres.techresfacebook.service.jbot.facebook.models.consume.CollectionFeed;
import com.techres.techresfacebook.service.jbot.facebook.models.consume.Comment;
import com.techres.techresfacebook.service.jbot.facebook.models.consume.Feed;
import com.techres.techresfacebook.service.twilio.TwilioClientService;
import com.techres.techresfacebook.service.twilio.data.ChannelCreateReqDTO;
import com.techres.techresfacebook.service.twilio.data.ChatPushReqDTO;
import com.techres.techresfacebook.service.twilio.data.MemberCreateReqDTO;
import com.techres.techresfacebook.service.twilio.data.UserCreateReqDTO;
import com.techres.techresfacebook.webrest.payload.ChatMessageReqDTO;
import io.vavr.control.Try;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class FacebookServiceImpl implements FacebookService {

    private static final Logger log = LoggerFactory.getLogger(FacebookServiceImpl.class);
    private String fbPageAccessToken = "EAAEdZBeRrYIABAL2437wAKq5uG5pUt6PdBkWWJMGFnyyIs3hdmsVdWuB3Nm4W4gMALPQEIlIkQ0iAyZCbTRnB5yZAfDv7BRytA9fU1XhJICrujeYy15NQ1xZCbIBoH0r9mKZAnQeksiklsyS3NcDDdOSnky8twgnK8YEpaptBBZCGY8NdkiZB8TcdWaeezpoMMZD";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FaceBookApiEndPoints faceBookApiEndPoints;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private TwilioClientService twilioClientService;

    @Override
    public User getUser(String credId) {
        return restTemplate.getForEntity(faceBookApiEndPoints.getFacebookUserApi(),User.class,credId,fbPageAccessToken).getBody();
    }

    @Override
    public Client getClientByCredId(String facebookId) {
        return clientRepository.findByCredentialPlatform(facebookId);
    }

    @Override
    public Try<Boolean> registerClientToSystem(String credId, String displayName) {

        twilioClientService.initTwilioServiceInstance();

        val user = new com.techres.techresfacebook.domain.User();
        user.setDisplayName(displayName);
        user.setNationality("UNKNOWN");
        user.setRole(Role.CLIENT);
        userRepository.save(user);

        val client = new Client();
        client.setCredentialPlatform(credId);
        client.setUser(user);
        clientRepository.save(client);

        val conversation = new Conversation();
        conversation.setDateCreated(Instant.now());
        conversation.setNameConversation("Conversation of "+displayName);
        conversation.setClient(client);
        val conversationCreated = conversationRepository.save(conversation);

        val participant = new Participant();
        participant.setTimeJoin(Instant.now());
        participant.setUser(user);
        participant.setConversation(conversationCreated);
        participantRepository.save(participant);

        val chat = new Chat();
        chat.setDateCreated(Instant.now());
        chat.setContent("Xin Chao");
        chat.setConversation(conversationCreated);
        chat.setSender(user);

        val userCreate = new UserCreateReqDTO();
        userCreate.setIndentity(credId);
        userCreate.setFriendlyName(displayName);
        twilioClientService.createUSer(userCreate);

        val channel = new ChannelCreateReqDTO();
        channel.setUniqueNameChannel("CHATBOX_" + conversationCreated.getId());
        channel.setFriendlyNameChannel("Conversation of "+displayName);
        twilioClientService.createChannel(channel);

        val member = new MemberCreateReqDTO();
        member.setChannelId("CHATBOX_" + conversationCreated.getId());
        member.setIdentity(credId);
        twilioClientService.createMember(member);

        return Try.of(() -> true);

    }

    @Override
    public Boolean sendMessageToUser(String userId, Message message) {
        User user = new User().setId(userId);
        Event event = new Event()
            .setRecipient(user)
            .setMessage(message)
            .setMessagingType("UPDATE")
            .setType(EventType.MESSAGE);

        restTemplate.postForEntity(faceBookApiEndPoints.getFacebookSendUrl(), event, Response.class, fbPageAccessToken);
        return true;
    }

    @Override
    public Try<Boolean> saveMessage(String message, Client client) {

        twilioClientService.initTwilioServiceInstance();
        // TODO : Find Conversation
        return conversationRepository.findByClientId(client.getId())
            .orElse(() -> Try.failure(new Exception("Failure when find conversation")))
            .flatMap(foundConversation -> {

                val chat = new Chat();
                chat.setDateCreated(Instant.now());
                chat.setContent(message);
                chat.setSender(client.getUser());
                chat.setConversation(foundConversation);
                chatRepository.save(chat);

                val chatPush = new ChatPushReqDTO();
                chatPush.setContent(message);
                chatPush.setFromSender(client.getUser().getDisplayName());
                chatPush.setUniqueIdChannel("CHATBOX_"+foundConversation.getId());
                return Try.of(() -> twilioClientService.pushChat(chatPush).get());
            });
    }

    @Override
    public Boolean sendText(String userId, ChatMessageReqDTO dto) {
        val message = new Message();
        message.setText(dto.getContent());
        return sendMessageToUser(userId, message);
    }

    @Override
    public Try<CollectionFeed> fetchCollectionFeed() {
        return Try.of(() -> {
            val feedData = restTemplate.getForEntity(faceBookApiEndPoints.fetchCollectionFeed(), Feed.class,this.fbPageAccessToken).getBody();
            log.info(">>>>> RESPONSE : " + feedData.toString());
            CollectionFeed collectionFeed = new CollectionFeed();
            collectionFeed.setFeed(feedData);
            return collectionFeed;
        });
    }

    @Override
    public Try<CollectionComment> fetchCollectionCommentByFeed(String feedId) {
        return Try.of(() -> {
            val commentData = restTemplate.getForEntity(faceBookApiEndPoints.fetchDetailFeedById(feedId), Comment.class,this.fbPageAccessToken).getBody();
            log.info(">>>>> RESPONSE : " + commentData.toString());
            CollectionComment collectionComment = new CollectionComment();
            collectionComment.setComment(commentData);
            return collectionComment;
        });
    }

}
