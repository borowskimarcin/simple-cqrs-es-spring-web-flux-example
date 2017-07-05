package com.marbor.social.app;


import com.marbor.social.app.aggregates.TweetsAggregate;
import com.marbor.social.app.commands.CreateTweetCommand;
import com.marbor.social.app.commands.MarkTweetAsCommand;
import com.marbor.social.app.events.TweetCreatedEvent;
import com.marbor.social.app.events.TweetReadEvent;
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
    public void giveAggregateRoot_whenCreateMessageCommand_thenShouldProduceMessageCreatedEvent() throws Exception {
        String eventText = "Hello, how is your day?";
        String id = UUID.randomUUID().toString();
        fixture.given()
                .when(new CreateTweetCommand(id, eventText))
                .expectEvents(new TweetCreatedEvent(id, eventText));
    }

    @Test
    public void givenMessageCreatedEvent_whenReadMessageCommand_thenShouldProduceMessageReadEvent() throws Exception {
        String id = UUID.randomUUID().toString();

        fixture.given(new TweetCreatedEvent(id, "Hello :-)"))
                .when(new MarkTweetAsCommand(id))
                .expectEvents(new TweetReadEvent(id));
    }
}