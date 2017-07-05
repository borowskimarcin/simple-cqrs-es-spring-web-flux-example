package com.marbor.social.app.events.user;

public class UserCreatedEvent
{

    private final String id;
    private final String name;

    public UserCreatedEvent(String id, String name) {
        this.id = id;
        this.name = name;
    }
 
    public String getId() {
        return id;
    }
 
    public String getName() {
        return name;
    }
}