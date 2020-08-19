package com.techres.techresfacebook.service.twilio.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techres.techresfacebook.service.twilio.TwilioClientService;
import com.techres.techresfacebook.service.twilio.data.ChannelCreateReqDTO;
import com.techres.techresfacebook.service.twilio.data.ChatPushReqDTO;
import com.techres.techresfacebook.service.twilio.data.MemberCreateReqDTO;
import com.techres.techresfacebook.service.twilio.data.UserCreateReqDTO;
import com.twilio.Twilio;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.ChatGrant;
import com.twilio.rest.chat.v2.service.Channel;
import com.twilio.rest.chat.v2.service.User;
import com.twilio.rest.chat.v2.service.channel.Member;
import com.twilio.rest.chat.v2.service.channel.Message;
import io.vavr.control.Try;
import lombok.val;
import org.joda.time.DateTime;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TwilioClientServiceImpl implements TwilioClientService {

    private static final String ACCOUNT_ID = "AC4af3eab2e4ea15148a250a3e542016cc";
    private static final String AUTH_TOKEN = "a91b7465b4df8bf5816643d1492cffe2";
    private static final String SERVICES_ID_CHAT = "IS350fb563eee749c8aee05e8e333eec9c";
    private static final String TWILIO_API_KEY = "SKbbf8a26c4301fc26358371a17e518e5a";
    private static final String TWILIO_SECRECT = "nOjSfuf94r6MlFxmddhUWQjK8RHlIavl";

    //TODO: Base to create instance
    @Override
    public void initTwilioServiceInstance(){
        Twilio.init(ACCOUNT_ID,AUTH_TOKEN);
    }

    @Override
    public Try<Boolean> createUSer(UserCreateReqDTO dto) {
        return Try.of(() -> {
            User user = User.creator(SERVICES_ID_CHAT,dto.getIndentity())
                    .setFriendlyName(dto.getFriendlyName())
                    .create();
            return (user instanceof User);
        });
    }

    @Override
    public Try<Boolean> fetchUser(String identity) {
        return Try.of(() -> {
            User userFetch = User.fetcher(SERVICES_ID_CHAT,identity).fetch();
            return (userFetch != null) ? true : false;
        });
    }

    @Override
    public Try<String> generateJwt(String email) {
        return Try.of(() -> {
            ChatGrant grantAuth = new ChatGrant();
            grantAuth.setServiceSid(SERVICES_ID_CHAT);
            AccessToken accessToken = new AccessToken.Builder(ACCOUNT_ID,TWILIO_API_KEY,TWILIO_SECRECT)
                    .identity(email).grant(grantAuth).build();
            return accessToken.toJwt();
        });
    }

    @Override
    public Try<Boolean> createChannel(ChannelCreateReqDTO dto) {
        return Try.of(() -> {
            Channel channel = Channel.creator(SERVICES_ID_CHAT)
                    .setUniqueName(dto.getUniqueNameChannel())
                    .setFriendlyName(dto.getFriendlyNameChannel())
                    .create();
            return (channel instanceof Channel);
        });
    }

    @Override
    public Try<Boolean> createMember(MemberCreateReqDTO dto) {
        return Try.of(() -> {
            Member member = Member.creator(SERVICES_ID_CHAT,dto.getChannelId(),dto.getIdentity())
                    .setDateCreated(DateTime.now()).create();
            return (member instanceof Member);
        });
    }

    @Override
    public Try<Boolean> pushChat(ChatPushReqDTO dto) {
        initTwilioServiceInstance();
        return Try.of(() -> {
            Message message = Message.creator(SERVICES_ID_CHAT,dto.getUniqueIdChannel())
                    .setDateCreated(DateTime.now())
                    .setFrom(dto.getFromSender())
                    .setBody(dto.getContent())
                    .create();
            return (message instanceof Message);
        });
    }

    @Override
    public HttpHeaders basicAuth() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(ACCOUNT_ID,AUTH_TOKEN);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return httpHeaders;
    }


    // ============================
    @Override
    public void deleteOneUser(String identity) {
        User.deleter(SERVICES_ID_CHAT,identity).delete();
    }

    @Override
    public void deleteOneChannel(String uniqueName) {
        Channel.deleter(SERVICES_ID_CHAT,uniqueName).delete();
    }

    @Override
    public Try<Boolean> deleteAllUserTwilio() {
        initTwilioServiceInstance();
        return Try.of(() -> {
            val httpHeader = basicAuth();
            String pathURI = "https://chat.twilio.com/v2/Services/" +SERVICES_ID_CHAT+"/Users";
            HttpEntity<?> request = new HttpEntity<>(httpHeader);
            //TODO: Make a request
            ResponseEntity<String> response = new RestTemplate().exchange(pathURI, HttpMethod.GET,request,String.class);
            ObjectMapper mapper = new ObjectMapper();
            //TODO: Convert to Json Object
            JsonNode jsonNode = mapper.readTree(response.getBody());
            JsonNode users = mapper.readTree(String.valueOf(jsonNode.get("users")));
            for (int i = 0 ; i < users.size() ; i++){
                deleteOneUser(users.get(i).get("identity").asText());
            }
            return true;
        });
    }

    @Override
    public Try<Boolean> deleteAllChannelTwilio() {
        initTwilioServiceInstance();
        return Try.of(() -> {
            val httpHeader = basicAuth();
            String pathURI = "https://chat.twilio.com/v2/Services/" +SERVICES_ID_CHAT+"/Channels";
            HttpEntity<?> request = new HttpEntity<>(httpHeader);
            //TODO: Make a request
            ResponseEntity<String> response = new RestTemplate().exchange(pathURI, HttpMethod.GET,request,String.class);
            ObjectMapper mapper = new ObjectMapper();
            //TODO: Convert to Json Object
            JsonNode jsonNode = mapper.readTree(response.getBody());
            JsonNode users = mapper.readTree(String.valueOf(jsonNode.get("channels")));
            for (int i = 0 ; i < users.size() ; i++){
                deleteOneChannel(users.get(i).get("unique_name").asText());
            }
            return true;
        });
    }
}
