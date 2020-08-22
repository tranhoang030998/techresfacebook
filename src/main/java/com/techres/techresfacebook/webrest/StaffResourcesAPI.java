package com.techres.techresfacebook.webrest;

import com.techres.techresfacebook.service.staff.StaffService;
import com.techres.techresfacebook.webrest.payload.StaffJoinConversationReqDTO;
import com.techres.techresfacebook.webrest.payload.StaffLoginReqDTO;
import com.techres.techresfacebook.webrest.payload.StaffRegisterReqDTO;
import com.techres.techresfacebook.webrest.response.RestResponseUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class StaffResourcesAPI {

    @Autowired
    private StaffService staffService;

    @PostMapping("/staff:register")
    public ResponseEntity<?> register(@RequestBody StaffRegisterReqDTO dto){
        val result = staffService.register(dto);
        return new ResponseEntity<>(RestResponseUtils.create(result,"Successfully",200,
                "Error when register Staff",500),HttpStatus.OK);
    }

    @PostMapping("/staff:login")
    public ResponseEntity<?> login(@RequestBody StaffLoginReqDTO dto){
        val result = staffService.login(dto);
        return new ResponseEntity<>(RestResponseUtils.create(result,"Successfully",200,
                "Wrong email or password, please try again!",500),HttpStatus.OK);
    }

    @PostMapping("/staff:joinConversation")
    public ResponseEntity<?> joinConversation(@RequestBody StaffJoinConversationReqDTO dto){
        val result = staffService.joinConversation(dto);
        return new ResponseEntity<>(RestResponseUtils.create(result,"Successfully",200,
                "Error when join to conversation",500),HttpStatus.OK);

    }

}
