package com.marbor.social.app.eventhandlers;

import com.marbor.social.app.events.TweetCreatedEvent;
import com.marbor.social.app.events.TweetReadEvent;
import org.axonframework.eventhandling.EventHandler;


public class TweetsEventHandler
{

    @EventHandler
    public void handle(TweetCreatedEvent event) {
        System.out.println("Message received: " + event.getText() + " (" + event.getId() + ")");
    }

    @EventHandler
    public void handle(TweetReadEvent event) {
        System.out.println("Message read: " + event.getId());
    }
}