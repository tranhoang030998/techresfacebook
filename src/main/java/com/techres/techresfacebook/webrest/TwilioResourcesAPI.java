package com.techres.techresfacebook.webrest;

import com.techres.techresfacebook.service.twilio.TwilioClientService;
import com.techres.techresfacebook.webrest.response.RestResponseUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class TwilioResourcesAPI {

    @Autowired
    private TwilioClientService twilioClientService;


    @GetMapping("/twilio:generateJwt")
    public ResponseEntity<?> generateJwt(@Param("email") String email){
        val result = twilioClientService.generateJwt(email);
        return new ResponseEntity<>(RestResponseUtils.create(result,"Success",200,
                "Failure generate jwt",500), HttpStatus.OK);
    }

    @DeleteMapping("/twilio:deleteAllUser")
    public ResponseEntity<?> deleteAllUser(){
        val result = twilioClientService.deleteAllUserTwilio();
        return new ResponseEntity<>(RestResponseUtils.create(result,"Success",200,
                "Failure delete all users",500), HttpStatus.OK);
    }

    @DeleteMapping("/twilio:deleteAllChannel")
    public ResponseEntity<?> deleteAllChannel(){
        val result = twilioClientService.deleteAllChannelTwilio();
        return new ResponseEntity<>(RestResponseUtils.create(result,"Success",200,
                "Failure delete all channels",500), HttpStatus.OK);
    }
}
