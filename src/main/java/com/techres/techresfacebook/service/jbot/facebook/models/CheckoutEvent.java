package com.techres.techresfacebook.service.jbot.facebook.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckoutEvent {

    private String object;
    private Entry[] entry;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Entry[] getEntry() {
        return entry;
    }

    public void setEntry(Entry[] entry) {
        this.entry = entry;
    }
}
