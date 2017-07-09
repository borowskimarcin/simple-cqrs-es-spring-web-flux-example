package com.marbor.social.app.routes;

/**
 * Created by marcin on 08.07.17.
 */
public enum Routes
{
    CREATE_USER("/users"),
    GET_USERS("/users"),
    GET_USER("/users/{id}"),
    SUBSCRIBE("/users/{id}/subscriptions/{followedId}"),
    GET_SUBSCRIPTIONS("/users/{id}/subscriptions"),
    CREATE_TWEET("/users/{id}/tweets"),
    GET_TWEETS("/tweets"),
    GET_TWEETS_WALL("/users/{id}/tweets/wall"),
    GET_TWEETS_TIMELINE("/users/{id}/tweets/timeline");

    private final String pattern;

    Routes(String pattern)
    {
        this.pattern = pattern;
    }

    public String pattern()
    {
        return pattern;
    }
}
