package com.techres.techresfacebook.service.jbot.facebook;

import com.techres.techresfacebook.service.jbot.common.BaseBot;
import com.techres.techresfacebook.service.jbot.common.Controller;
import com.techres.techresfacebook.service.jbot.common.EventType;
import com.techres.techresfacebook.service.jbot.endpoint.FaceBookApiEndPoints;
import com.techres.techresfacebook.service.jbot.facebook.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;

public abstract class Bot extends BaseBot {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    // TODO : Init URL for BOT
    private String facebookSendUrl;
    private String facebookMessengerProfileUrl;

    @Autowired
    protected RestTemplate restTemplate;
    @Autowired
    protected FaceBookApiEndPoints faceBookApiEndPoints;

    @PostConstruct
    private void constructFbSendUrl() {

        facebookSendUrl = faceBookApiEndPoints.getFacebookSendUrl().replace("{PAGE_ACCESS_TOKEN}", getPageAccessToken());
        facebookMessengerProfileUrl = faceBookApiEndPoints.getFacebookMessengerProfileUrl().replace("{PAGE_ACCESS_TOKEN}",getPageAccessToken());
    }


    public abstract String getFbToken();

    public abstract String getPageAccessToken();
    public abstract void defaultControllerInvoke(Event event) throws Throwable;

    @GetMapping("/WebHook")
    public final ResponseEntity setupWebHookVerification(@RequestParam("hub.mode") String mode,
                                                         @RequestParam("hub.verify_token") String verifyToken,
                                                         @RequestParam("hub.challenge") String challenge) {
        if (EventType.SUBSCRIBE.name().equalsIgnoreCase(mode) && getFbToken().equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @ResponseBody
    @PostMapping("/WebHook")
    public final ResponseEntity setupWebHookEndpoint(@RequestBody Callback callback) {
        try {
            // Checks this is an event from a page subscription
            if (!callback.getObject().equals("page")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.debug("Callback from fb: {}", callback);
            for (Entry entry : callback.getEntry()) {
                if (entry.getMessaging() != null) {
                    for (Event event : entry.getMessaging()) {
                        if (event.getMessage() != null) {
                            if (event.getMessage().isEcho() != null && event.getMessage().isEcho()) {
                                event.setType(EventType.MESSAGE_ECHO);
                            } else if (event.getMessage().getQuickReply() != null) {
                                event.setType(EventType.QUICK_REPLY);
                            } else {
                                event.setType(EventType.MESSAGE);
                                // send typing on indicator to create a conversational experience
                                sendTypingOnIndicator(event.getSender());
                            }
                        } else if (event.getDelivery() != null) {
                            event.setType(EventType.MESSAGE_DELIVERED);
                        } else if (event.getRead() != null) {
                            event.setType(EventType.MESSAGE_READ);
                        } else if (event.getPostback() != null) {
                            event.setType(EventType.POSTBACK);
                        } else if (event.getOptin() != null) {
                            event.setType(EventType.OPT_IN);
                        } else if (event.getReferral() != null) {
                            event.setType(EventType.REFERRAL);
                        } else if (event.getAccountLinking() != null) {
                            event.setType(EventType.ACCOUNT_LINKING);
                        } else {
                            logger.debug("Callback/Event type not supported: {}", event);
                            return ResponseEntity.ok("Callback not supported yet!");
                        }
                        if (isConversationOn(event)) {
                            invokeChainedMethod(event);
                        } else {
                            invokeMethods(event);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error in fb WebHook: Callback: {} \nException: ", callback.toString(), e);
        }
        // fb advises to send a 200 response within 20 secs
        return ResponseEntity.ok("EVENT_RECEIVED");
    }

    // ========================================================== METHOD SUPPORT BOT
    private void sendTypingOnIndicator(User recipient) {
        restTemplate.postForEntity(facebookSendUrl,new Event().setRecipient(recipient).setSenderAction("typing_on"), Response.class);
    }

    private void sendTypingOffIndicator(User recipient) {
        restTemplate.postForEntity(facebookSendUrl,new Event().setRecipient(recipient).setSenderAction("typing_off"), Response.class);
    }

    protected final ResponseEntity<String> reply(Event event) {
        sendTypingOffIndicator(event.getRecipient());
        logger.debug("Send message: {}", event.toString());
        try {
            return restTemplate.postForEntity(facebookSendUrl, event, String.class);
        } catch (HttpClientErrorException e) {
            logger.error("Send message error: Response body: {} \nException: ", e.getResponseBodyAsString(), e);
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    protected ResponseEntity<String> reply(Event event, String text) {
        Event response = new Event()
                .setMessagingType("RESPONSE")
                .setRecipient(event.getSender())
                .setMessage(new Message().setText(text));
        return reply(response);
    }

    protected ResponseEntity<String> reply(Event event, Message message) {
        Event response = new Event()
                .setMessagingType("RESPONSE")
                .setRecipient(event.getSender())
                .setMessage(message);
        return reply(response);
    }


    protected final ResponseEntity<Response> setGetStartedButton(String payload) {
        Event event = new Event().setGetStarted(new Postback().setPayload(payload));
        return restTemplate.postForEntity(facebookMessengerProfileUrl, event, Response.class);
    }

    protected final ResponseEntity<Response> setGreetingText(Payload[] greeting) {
        Event event = new Event().setGreeting(greeting);
        return restTemplate.postForEntity(facebookMessengerProfileUrl, event, Response.class);
    }

    @PostMapping("/subscribe")
    public final void subscribeAppToPage() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("access_token", getPageAccessToken());
        restTemplate.postForEntity(faceBookApiEndPoints.getFacebookSubscribeUrl(), params, String.class);
    }

    protected final void startConversation(Event event, String methodName) {
        startConversation(event.getSender().getId(), methodName);
    }


    protected final void nextConversation(Event event) {
        nextConversation(event.getSender().getId());
    }


    protected final void stopConversation(Event event) {
        stopConversation(event.getSender().getId());
    }


    protected final boolean isConversationOn(Event event) {
        return isConversationOn(event.getSender().getId());
    }


    private void invokeMethods(Event event) {
        boolean isInvoked = false;
        try {
            List<MethodWrapper> methodWrappers = eventToMethodsMap.get(event.getType().name().toUpperCase());
            if (methodWrappers == null) return;

            methodWrappers = new ArrayList<>(methodWrappers);
            MethodWrapper matchedMethod =
                    getMethodWithMatchingPatternAndFilterUnmatchedMethods(getPatternFromEventType(event), methodWrappers);
            if (matchedMethod != null) {
                methodWrappers = new ArrayList<>();
                methodWrappers.add(matchedMethod);
            }

            for (MethodWrapper methodWrapper : methodWrappers) {
                Method method = methodWrapper.getMethod();
                if (Arrays.asList(method.getParameterTypes()).contains(Matcher.class)) {
                    method.invoke(this, event, methodWrapper.getMatcher());
                } else {
                    method.invoke(this, event);
                }
                isInvoked = true;
            }
        } catch (Exception e) {
            logger.error("Error invoking controller: ", e);
            isInvoked = true;
        } finally {
            if (!isInvoked) {
                try {
                    defaultControllerInvoke(event);
                } catch (Throwable e) {
                    logger.error("Error invoking Default Controller: ", e.getCause());
                }
            }
        }
    }


    private void invokeChainedMethod(Event event) {
        Queue<MethodWrapper> queue = conversationQueueMap.get(event.getSender().getId());

        if (queue != null && !queue.isEmpty()) {
            MethodWrapper methodWrapper = queue.peek();

            try {
                EventType[] eventTypes = methodWrapper.getMethod().getAnnotation(Controller.class).events();
                for (EventType eventType : eventTypes) {
                    if (eventType.name().equalsIgnoreCase(event.getType().name())) {
                        methodWrapper.getMethod().invoke(this, event);
                        return;
                    }
                }
            } catch (Exception e) {
                logger.error("Error invoking chained method: ", e);
            }
        }
    }


    private String getPatternFromEventType(Event event) {
        switch (event.getType()) {
            case MESSAGE:
                return event.getMessage().getText();
            case QUICK_REPLY:
                return event.getMessage().getQuickReply().getPayload();
            case POSTBACK:
                return event.getPostback().getPayload();
            default:
                return event.getMessage().getText();
        }
    }
}
