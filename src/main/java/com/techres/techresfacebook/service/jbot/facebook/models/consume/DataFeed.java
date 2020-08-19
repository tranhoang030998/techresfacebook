package com.techres.techresfacebook.service.jbot.facebook.models.consume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author duy hoang
 * @version 05/08/2020
 */
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataFeed {

    private String id;
    private String message;
    private String story;
    private String full_picture;
    private String created_time;

}
