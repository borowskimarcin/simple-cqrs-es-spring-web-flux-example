package com.marbor.social.app.domain;

import lombok.Data;

import java.util.UUID;

/**
 * Created by marcin on 06.07.17.
 */
@Data
public class Tweet implements DomainObject
{
    private final String id = UUID.randomUUID().toString();
    private final String text;

    public Tweet(String text)
    {
        this.text = text;
    }

    @Override
    public String getId()
    {
        return id;
    }
}

