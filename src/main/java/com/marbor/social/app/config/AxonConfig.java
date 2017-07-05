package com.marbor.social.app.config;

import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;

/**
 * Created by marcin on 08.07.17.
 */
public class AxonConfig implements Config
{
    private final SimpleCommandBus commandBus = new SimpleCommandBus();
    private final EventStore eventStore = new EmbeddedEventStore(new InMemoryEventStorageEngine());
    private final CommandGateway commandGateway = new DefaultCommandGateway(commandBus);

    @Override
    public void init()
    {
        new TweetsAxonConfig(commandBus, eventStore).init();
        new UsersAxonConfig(commandBus, eventStore).init();
    }

    public CommandGateway getCommandGateway()
    {
        return commandGateway;
    }
}
