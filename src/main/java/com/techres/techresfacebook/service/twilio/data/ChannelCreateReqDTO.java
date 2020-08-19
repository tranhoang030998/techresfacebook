package com.techres.techresfacebook.service.twilio.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelCreateReqDTO {

    private String uniqueNameChannel;
    private String friendlyNameChannel;

}
