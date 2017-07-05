package com.marbor.social.app;


import com.marbor.social.app.aggregates.UsersAggregate;
import com.marbor.social.app.commands.user.AddFollowerCommand;
import com.marbor.social.app.commands.user.CreateUserCommand;
import com.marbor.social.app.events.user.FollowerAddedEvent;
import com.marbor.social.app.events.user.UserCreatedEvent;
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
    public void giveAggregateRoot_whenCreateUserCommand_thenShouldProduceUserCreatedEvent() throws Exception {
        String name = "user";
        String id = UUID.randomUUID().toString();
        fixture.given()
                .when(new CreateUserCommand(id, name))
                .expectEvents(new UserCreatedEvent(id, name));
    }
    @Test
    public void giveAggregateRoot_whenAddFollowerCommand_thenShouldProduceFollowerAddedEvent() throws Exception {
        String id = UUID.randomUUID().toString();
        String followerId = UUID.randomUUID().toString();
        String name = "name";
        fixture.given(new UserCreatedEvent(id, name))
                .when(new AddFollowerCommand(id, followerId))
                .expectEvents(new FollowerAddedEvent(id, followerId));
    }
}