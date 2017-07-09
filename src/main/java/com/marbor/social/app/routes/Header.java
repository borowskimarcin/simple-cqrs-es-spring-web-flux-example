package com.marbor.social.app.routes;

/**
 * Created by marcin on 08.07.17.
 */
public enum Header
{
    ERROR("Error");

    private final String message;

    Header(String message)
    {
        this.message = message;
    }

    public String message()
    {
        return message;
    }
}
