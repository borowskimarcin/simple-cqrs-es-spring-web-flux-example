package com.marbor.social.app.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by marcin on 05.07.17.
 */
public class User implements DomainObject
{
    private static final String NAME_CAN_NOT_BE_NULL = "Name can not be null";

    @NotNull
    private final String id;
    @NotNull
    private final String name;
    @NotNull
    /**
     * List of follower's id
     */
    private final List<String> followersIds;
    private final List<String> followedIds;
    @JsonCreator
    public User(@JsonProperty("name") String name)
    {
        this(name, UUID.randomUUID().toString());
    }

    public User(String name, String id)
    {
        Objects.requireNonNull(name, NAME_CAN_NOT_BE_NULL);
        this.followersIds = new ArrayList<>();
        this.followedIds = new ArrayList<>();
        this.name = name;
        this.id = id;
    }

    @NotNull
    @Override
    public String getId()
    {
        return id;
    }

    @NotNull
    public String getName()
    {
        return name;
    }

    @NotNull
    public List<String> getFollowersIds()
    {
        return followersIds;
    }

    @NotNull
    public List<String> getFollowedIds()
    {
        return followedIds;
    }
}
