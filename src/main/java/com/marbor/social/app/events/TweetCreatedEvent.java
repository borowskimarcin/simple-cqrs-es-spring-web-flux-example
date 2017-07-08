package com.marbor.social.app.events;

public class TweetCreatedEvent
{
    private final String id;
    private final String message;
    private final String authorId;

    public TweetCreatedEvent(String id, String message, String authorId) {
        this.id = id;
        this.message = message;
        this.authorId = authorId;
    }
 
    public String getId() {
        return id;
    }
 
    public String getMessage() {
        return message;
    }

    public String getAuthorId()
    {
        return authorId;
    }
}