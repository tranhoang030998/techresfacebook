package com.techres.techresfacebook.service.jbot.facebook.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

public class Messages {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Content {
        private String id;
        private User from;
        @JsonProperty("created_time")
        private Instant createdTime;
        private String message;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private String name;
        private String email;
        private String id;
    }

    private List<Content> data;
    private PageInfo paging;

}
