package com.techres.techresfacebook.service.jbot.facebook.models;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversations {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Content {
        private String id;
    }

    private List<Content> data;
    private PageInfo paging;

}
