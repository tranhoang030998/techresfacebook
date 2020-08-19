package com.techres.techresfacebook.service.twilio.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatPushReqDTO {

    private String uniqueIdChannel;
    private String fromSender;
    private String content;

}
