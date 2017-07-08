package com.marbor.social.app;


import com.marbor.social.app.aggregates.UsersAggregate;
import com.marbor.social.app.commands.SubscribeCommand;
import com.marbor.social.app.commands.CreateUserCommand;
import com.marbor.social.app.events.SubscribeEvent;
import com.marbor.social.app.events.UserCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class UsersAggregateIntegrationTest
{

    private FixtureConfiguration<UsersAggregate> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture<>(UsersAggregate.class);

    }

    @Test
    public void testCreateUserCommand() throws Exception {
        String name = "user";
        String id = UUID.randomUUID().toString();
        fixture.given()
                .when(new CreateUserCommand(id, name))
                .expectEvents(new UserCreatedEvent(id, name));
    }
    @Test
    public void testAddFollowerCommand() throws Exception {
        String id = UUID.randomUUID().toString();
        String followedId = UUID.randomUUID().toString();
        String name = "name";
        fixture.given(new UserCreatedEvent(id, name))
                .when(new SubscribeCommand(id, followedId))
                .expectEvents(new SubscribeEvent(id, followedId));
    }
}