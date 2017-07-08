package com.marbor.social.app.events;

public class SubscribeEvent
{
    private final String userId;
    private final String followedId;

    public SubscribeEvent(String userId, String followedId) {
        this.userId = userId;
        this.followedId = followedId;
    }
 
    public String getUserId() {
        return userId;
    }
 
    public String getFollowedId() {
        return followedId;
    }
}