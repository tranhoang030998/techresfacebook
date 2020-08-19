package com.techres.techresfacebook.webrest;

import com.techres.techresfacebook.service.conversation.ConversationService;
import com.techres.techresfacebook.webrest.response.RestResponseUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ConversationResourcesAPI {

    @Autowired
    private ConversationService conversationService;

    @GetMapping("/conversation:fetchAll")
    public ResponseEntity<?> fetchAllConversation(){
        val result = conversationService.fetchAllConversation();
        return new ResponseEntity<>(RestResponseUtils.createList(result,"Success",200), HttpStatus.OK);
    }

}
