package com.techres.techresfacebook.webrest;

import com.techres.techresfacebook.service.chat.ChatService;
import com.techres.techresfacebook.service.jbot.facebook.FacebookService;
import com.techres.techresfacebook.webrest.payload.ChatMessageReqDTO;
import com.techres.techresfacebook.webrest.response.RestResponseUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class  ChatResourcesAPI {

    @Autowired
    private ChatService chatService;

    @Autowired
    private FacebookService facebookService;

    @GetMapping("/chat:fetchByConversation/{id}")
    public ResponseEntity<?> fetchAllByConversation(@PathVariable("id") Long id) {
        val result = chatService.fetchChatByConversation(id);
        return new ResponseEntity<>(RestResponseUtils.createList(result,"Success",200), HttpStatus.OK);
    }

    @PostMapping("/chat:sendMessageToFacebook")
    public ResponseEntity<?> sendMessageToFacebookUser(@RequestBody ChatMessageReqDTO dto) throws URISyntaxException {
        val result = chatService.sendMessageToFacebook(dto);
        return new ResponseEntity<>(RestResponseUtils.create(result,"Successfully",200,
                "Error when send Message To Facebook",500),HttpStatus.OK);
    }


}
