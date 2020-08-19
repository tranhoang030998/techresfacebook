package com.techres.techresfacebook.webrest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericBaseResponse <T> {

    T data;
    Meta meta;

    @Data
    public static class Meta {
        private String status;
        private String errorMessage;
        private Integer errorCode;
    }
}
