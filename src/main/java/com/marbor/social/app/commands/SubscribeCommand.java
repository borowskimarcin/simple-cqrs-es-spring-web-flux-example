package com.marbor.social.app.commands;


import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class SubscribeCommand
{

    @TargetAggregateIdentifier
    private final String id;
    private final String followedId;

    public SubscribeCommand(String id, String followedId) {
        this.id = id;
        this.followedId = followedId;
    }
 
    public String getId() {
        return id;
    }
 
    public String getFollowedId() {
        return followedId;
    }
}