package com.marbor.social.app.events;

public class TweetReadEvent
{
 
    private final String id;
 
    public TweetReadEvent(String id) {
        this.id = id;
    }
 
    public String getId() {
        return id;
    }
}