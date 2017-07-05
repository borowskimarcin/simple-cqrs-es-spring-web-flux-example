package com.marbor.social.app.commands.user;


import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class AddFollowerCommand
{

    @TargetAggregateIdentifier
    private final String id;
    private final String followerId;

    public AddFollowerCommand(String id, String followerId) {
        this.id = id;
        this.followerId = followerId;
    }
 
    public String getId() {
        return id;
    }
 
    public String getFollowerId() {
        return followerId;
    }
}