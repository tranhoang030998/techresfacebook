package com.techres.techresfacebook.service.jbot.facebook.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cursors {
        private String after;
        private String before;
    }

    private Cursors cursors;
    private String previous;
    private String next;
}
