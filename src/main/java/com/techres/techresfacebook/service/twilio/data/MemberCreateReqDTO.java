package com.techres.techresfacebook.service.twilio.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateReqDTO {

    private String channelId;
    private String identity;

}
