package com.techres.techresfacebook.webrest.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageReqDTO {

    private Long staffId;
    private Long conversationId;
    private String content;

}
