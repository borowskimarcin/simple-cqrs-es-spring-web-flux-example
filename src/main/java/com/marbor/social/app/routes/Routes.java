package com.marbor.social.app.routes;

/**
 * Created by marcin on 08.07.17.
 */
public enum Routes
{
    CREATE_USER("/users"),
    GET_USERS("/users"),
    GET_USER("/users/{id}"),
    ADD_FOLLOWER("/users/{id}/followers/{followerId}"),
    GET_FOLLOWERS("/users/{id}/followers");

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
