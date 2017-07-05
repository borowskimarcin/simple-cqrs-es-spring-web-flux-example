package com.marbor.social.app.events.user;

public class FollowerAddedEvent
{
    private final String userId;
    private final String followerId;

    public FollowerAddedEvent(String userId, String followerId) {
        this.userId = userId;
        this.followerId = followerId;
    }
 
    public String getUserId() {
        return userId;
    }
 
    public String getFollowerId() {
        return followerId;
    }
}