package com.techres.techresfacebook.webrest.bot;

import com.techres.techresfacebook.domain.Client;
import com.techres.techresfacebook.service.jbot.common.Controller;
import com.techres.techresfacebook.service.jbot.common.EventType;
import com.techres.techresfacebook.service.jbot.common.JBot;
import com.techres.techresfacebook.service.jbot.facebook.Bot;
import com.techres.techresfacebook.service.jbot.facebook.FacebookService;
import com.techres.techresfacebook.service.jbot.facebook.models.Event;
import com.techres.techresfacebook.service.jbot.facebook.models.Message;
import com.techres.techresfacebook.service.jbot.facebook.models.Payload;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

@JBot
public class FacebookBot extends Bot {

    private String fbToken = "fb_token_techres_bot";
    private String pageAccessToken = "EAAEdZBeRrYIABAGyZBLEppiZAiu1li6effty13uxCZB6HsbQnRWKbDQ7l3vDM7UZAc1ECAo5ISfRFexkhTR9AlLeX47ly6vkMgaBLR7FBWGslsQAdXnSZCquVz44O1DREIlEGuMEeAQaXY2rsWmGp0vQmzt7n72Mh8dc0ax5lRsDojdVt5FSX9IFcqug43ipEZD";

    @Autowired
    private FacebookService facebookService;

    @Override
    public String getFbToken() {
        return fbToken;
    }

    @Override
    public String getPageAccessToken() {
        return pageAccessToken;
    }

//    @PostConstruct
    public void initialize() {
        setGreetingText(new Payload[]{new Payload().setLocale("default").setText("Hello, welcome to Techres FanPage, you just need click" +
            "the \"Get Started\" button or just typing (Hi,hello,hey).")});
        setGetStartedButton("Get Started");
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK}, pattern = "^(?i)(hi|hello|hey)$")
    public void onGetStarted(Event event) throws Throwable {

        System.out.println("USER Space scope in Facebook FanPage Id: " + event.getSender().getId());

        val facebookId = event.getSender().getId();
        val client = facebookService.getClientByCredId(facebookId);
        if (client == null) {
            System.out.println("USER NOT ALREADY EXISTING");
            // TODO: First time client come system
            val userFacebook = facebookService.getUser(facebookId);
            facebookService.registerClientToSystem(facebookId,userFacebook.getFirstName() + " " + userFacebook.getLastName());
            reply(event, new Message().setText("(❀◕ ‿ ◕❀) Bạn Đã Connect Với FanPage Thành Công.  \uD83D\uDE07"));
        } else {
            System.out.println("USER ALREADY EXISTING");
            reply(event, new Message().setText("(❀◕ ‿ ◕❀) Chờ chút nhé!!  \uD83D\uDE07"));
        }
    }

    @Controller(events = EventType.MESSAGE)
    public void onChatTime(Event event){
        val facebookId = event.getSender().getId();
        val client = facebookService.getClientByCredId(facebookId);
        if (client instanceof Client){
            facebookService.saveMessage(event.getMessage().getText(),client);
        }
        System.out.println(">>>>>>>>>>>>>>>> : Chat of user: " + event.getMessage().getText());
    }

    @Override
    public void defaultControllerInvoke(Event event) throws Throwable {
        if (event.getType() == EventType.MESSAGE) {
            // TODO : Nothing at this time
        }
    }

}
