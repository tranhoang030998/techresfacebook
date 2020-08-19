package com.techres.techresfacebook.service.twilio;

import com.techres.techresfacebook.service.twilio.data.ChannelCreateReqDTO;
import com.techres.techresfacebook.service.twilio.data.ChatPushReqDTO;
import com.techres.techresfacebook.service.twilio.data.MemberCreateReqDTO;
import com.techres.techresfacebook.service.twilio.data.UserCreateReqDTO;
import io.vavr.control.Try;
import org.springframework.http.HttpHeaders;

public interface TwilioClientService {

    void initTwilioServiceInstance();

    Try<Boolean> createUSer(UserCreateReqDTO dto);

    Try<Boolean> fetchUser(String identity);

    Try<String> generateJwt(String email);

    Try<Boolean> createChannel(ChannelCreateReqDTO dto);

    Try<Boolean> createMember(MemberCreateReqDTO dto);

    Try<Boolean> pushChat(ChatPushReqDTO dto);

    HttpHeaders basicAuth();

    // ====================================
    void deleteOneUser(String identity);
    void deleteOneChannel(String uniqueName);

    Try<Boolean> deleteAllUserTwilio();
    Try<Boolean> deleteAllChannelTwilio();

}
