package com.techres.techresfacebook.service.jbot.endpoint.impl;

import com.techres.techresfacebook.service.jbot.endpoint.FaceBookApiEndPoints;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FaceBookApiEndPointsImpl implements FaceBookApiEndPoints {

    /**
     * Endpoint for Fb Api
     */
    private final String fbGraphApi = "https://graph.facebook.com/v4.0";

    public FaceBookApiEndPointsImpl() {
    }
    @Override
    public String getFacebookUserApi() {
        return fbGraphApi + "/{userId}?access_token={token}";
    }
    @Override
    public String getFacebookSendUrl() {
        return fbGraphApi + "/me/messages?access_token={PAGE_ACCESS_TOKEN}";
    }

    @Override
    public String getFacebookSubscribeUrl() {
        return fbGraphApi + "/me/subscribed_apps";
    }

    @Override
    public String getFacebookMessageUrl() {
        return fbGraphApi + "/{conversationId}/messages?date_format=U&fields=created_time,from,message&limit={lm}&access_token={PAGE_ACCESS_TOKEN}";
    }

    @Override
    public String getFacebookMessengerProfileUrl() {
        return fbGraphApi + "/me/messenger_profile?access_token={PAGE_ACCESS_TOKEN}";
    }
//
//    // = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = //
//
    @Override
    public String fetchCollectionFeed() {
        return fbGraphApi + "/me/feed?fields=id,message,story,full_picture,created_time&access_token={PAGE_ACCESS_TOKEN}";
    }

    @Override
    public String fetchDetailFeedById(String feedId) {
        return fbGraphApi + "/" + feedId + "/comments?fields=id,message,created_time,attachment&access_token={PAGE_ACCESS_TOKEN}";
    }

}
