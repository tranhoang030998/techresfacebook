package com.techres.techresfacebook.service.jbot.facebook.models.consume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Paging {

    private Cursors cursors;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cursors {
        private String before;
        private String after;
    }


}
