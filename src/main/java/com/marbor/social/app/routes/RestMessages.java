package com.marbor.social.app.routes;

/**
 * Created by marcin on 08.07.17.
 */
public enum RestMessages
{
    USER_ALREADY_EXISTS("User already exists"),
    USER_NOT_FOUND("User not found"),
    USERS_NOT_FOUND("Users do not exists"),
    SUBSCRIPTION_NOT_FOUND("Subscription not found"),
    TWEET_MESSAGE_TOO_LONG("Message can not have more than 140 chars");

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
