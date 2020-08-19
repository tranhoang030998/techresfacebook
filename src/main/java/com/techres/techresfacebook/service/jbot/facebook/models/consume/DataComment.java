package com.techres.techresfacebook.service.jbot.facebook.models.consume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author duy hoang
 * @version 05/08/2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataComment {

    private String id;
    private String message;
    private String created_time;
    private Attachment attachment;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attachment {

        private Media media;
        private Target target;
        private String type;
        private String url;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Media {

            private Image image;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Image {
                private int width;
                private int height;
                private String src;
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Target {

            private String id;
            private String url;
        }
    } // Attachment


}
