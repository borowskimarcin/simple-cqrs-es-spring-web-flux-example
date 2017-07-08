package com.marbor.social.app.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by marcin on 06.07.17.
 */
public class Tweet implements DomainObject
{
    private final String id;
    private final String ownerId;
    private final String authorId;
    private final String message;

    @JsonCreator
    public Tweet(@JsonProperty("message") String message, @JsonProperty("authorId") String authorId)
    {
        this(UUID.randomUUID().toString(), message, authorId, authorId);
    }

    public Tweet(String id, String message, String ownerId, String authorId)
    {
        this.id = id;
        this.message = message;
        this.ownerId = ownerId;
        this.authorId = authorId;
    }

    @Override
    public String getId()
    {
        return id;
    }

    public String getMessage()
    {
        return message;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public String getAuthorId()
    {
        return authorId;
    }
}

