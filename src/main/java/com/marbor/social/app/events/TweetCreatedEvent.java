package com.marbor.social.app.events;

public class TweetCreatedEvent
{
 
    private final String id;
    private final String text;
 
    public TweetCreatedEvent(String id, String text) {
        this.id = id;
        this.text = text;
    }
 
    public String getId() {
        return id;
    }
 
    public String getText() {
        return text;
    }
}