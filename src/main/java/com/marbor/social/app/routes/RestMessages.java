package com.marbor.social.app.routes;

/**
 * Created by marcin on 08.07.17.
 */
public enum RestMessages
{
    USER_ALREADY_EXISTS("User already exists");

    private final String message;

    RestMessages(String message)
    {
        this.message = message;
    }

    public String message()
    {
        return message;
    }
}
