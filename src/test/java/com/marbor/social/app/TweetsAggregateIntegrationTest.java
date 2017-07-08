package com.marbor.social.app;


import com.marbor.social.app.aggregates.TweetsAggregate;
import com.marbor.social.app.commands.CreateTweetCommand;
import com.marbor.social.app.events.TweetCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class TweetsAggregateIntegrationTest
{

    private FixtureConfiguration<TweetsAggregate> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture<>(TweetsAggregate.class);

    }

    @Test
    public void testCreateMessageCommand() throws Exception {
        String message = "Hello, how is your day?";
        String id = UUID.randomUUID().toString();
        String authorId = UUID.randomUUID().toString();
        fixture.given()
                .when(new CreateTweetCommand(id, message, authorId))
                .expectEvents(new TweetCreatedEvent(id, message, authorId));
    }
}