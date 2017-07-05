package com.marbor.social.app.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by marcin on 05.07.17.
 */
public class User implements DomainObject
{
    private static final String NAME_CAN_NOT_BE_NULL = "Name can not be null";
    private static final String FOLLOWERS_LIST_CAN_NOT_BE_NULL = "Followers list can not be null";

    @NotNull
    private final String id;
    @NotNull
    private final String name;
    @NotNull
    /**
     * List of follower's id
     */
    private List<String> followers;

    @JsonCreator
    public User(@JsonProperty("name") String name)
    {
        this(name, UUID.randomUUID().toString());
    }

    public User(String name, String id)
    {
        this(name, id, Collections.emptyList());
    }

    public User(@NotNull String name, String id, @NotNull List<String> followers)
    {
        Objects.requireNonNull(name, NAME_CAN_NOT_BE_NULL);
        Objects.requireNonNull(followers, FOLLOWERS_LIST_CAN_NOT_BE_NULL);
        this.followers = followers.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
    public List<String> getFollowers()
    {
        return followers;
    }
}
