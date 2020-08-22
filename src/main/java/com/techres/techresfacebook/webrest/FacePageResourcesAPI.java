package com.techres.techresfacebook.webrest;

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
public class FacePageResourcesAPI {

    @Autowired
    private FacebookService facebookService;

    @GetMapping("/fetchAllPostInFanPage")
    public ResponseEntity<?> fetchAllPostInFanPage() throws URISyntaxException {
        val result = facebookService.fetchCollectionFeed();
        return new ResponseEntity<>(RestResponseUtils.create(result,"Successfully",200,
                "Error when fetch all feed",500),HttpStatus.ACCEPTED);
    }

    @GetMapping("/fetchAllCommentByPost/{feedId}")
    public ResponseEntity<?> fetchAllCommentByPost(@PathVariable String feedId) throws URISyntaxException {
        val result = facebookService.fetchCollectionCommentByFeed(feedId);
        return new ResponseEntity<>(RestResponseUtils.create(result,"Successfully",200,
                "Error when fetch all comment on Post",500),HttpStatus.OK);
    }

}
