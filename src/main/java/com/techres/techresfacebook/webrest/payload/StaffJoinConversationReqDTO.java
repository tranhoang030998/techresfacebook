package com.techres.techresfacebook.webrest.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffJoinConversationReqDTO {

    private Long staffId;
    private Long conversationId;
}
