package com.marbor.social.app.config;

import com.marbor.social.app.aggregates.UsersAggregate;
import com.marbor.social.app.eventhandlers.UsersEventHandler;
import org.axonframework.commandhandling.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.AnnotationEventListenerAdapter;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;

/**
 * Created by marcin on 08.07.17.
 */

class UsersAxonConfig implements Config
{
    private final CommandBus commandBus;
    private final EventStore eventStore;

    UsersAxonConfig(CommandBus commandBus, EventStore eventStore)
    {
        this.commandBus = commandBus;
        this.eventStore = eventStore;
    }

    @Override
    public void init()
    {
        EventSourcingRepository<UsersAggregate> repository =
                new EventSourcingRepository<>(UsersAggregate.class, eventStore);

        setUpAggregate(repository);

        final AnnotationEventListenerAdapter annotationEventListenerAdapter =
                new AnnotationEventListenerAdapter(new UsersEventHandler());

        eventStore.subscribe(eventMessages -> eventMessages
                .forEach(event -> handleEvent(annotationEventListenerAdapter, event)));
    }

    private void setUpAggregate(EventSourcingRepository<UsersAggregate> repository)
    {
        AggregateAnnotationCommandHandler<UsersAggregate> userAggregateAnnotationCommandHandler =
                new AggregateAnnotationCommandHandler<>(UsersAggregate.class, repository);
        userAggregateAnnotationCommandHandler.subscribe(commandBus);
    }

    private void handleEvent(AnnotationEventListenerAdapter annotationEventListenerAdapter, EventMessage<?> event)
    {
        try
        {
            annotationEventListenerAdapter.handle(event);
        } catch (Exception e)
        {
            throw new RuntimeException(e);

        }
    }
}
