package com.marbor.social.app.config;

import com.marbor.social.app.aggregates.TweetsAggregate;
import com.marbor.social.app.eventhandlers.TweetsEventHandler;
import org.axonframework.commandhandling.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.AnnotationEventListenerAdapter;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;

/**
 * Created by marcin on 08.07.17.
 */

class TweetsAxonConfig implements Config
{
    private final CommandBus commandBus;
    private final EventStore eventStore;

    TweetsAxonConfig(CommandBus commandBus, EventStore eventStore)
    {
        this.commandBus = commandBus;
        this.eventStore = eventStore;
    }

    @Override
    public void init()
    {
        EventSourcingRepository<TweetsAggregate> repository =
                new EventSourcingRepository<>(TweetsAggregate.class, eventStore);

        setUpAggregate(repository);

        final AnnotationEventListenerAdapter annotationEventListenerAdapter =
                new AnnotationEventListenerAdapter(new TweetsEventHandler());

        eventStore.subscribe(eventMessages -> eventMessages
                .forEach(event -> handleEvent(annotationEventListenerAdapter, event)));
    }

    private void setUpAggregate(EventSourcingRepository<TweetsAggregate> repository)
    {
        AggregateAnnotationCommandHandler<TweetsAggregate> twitterAggregateAnnotationCommandHandler =
                new AggregateAnnotationCommandHandler<>(TweetsAggregate.class, repository);
        twitterAggregateAnnotationCommandHandler.subscribe(commandBus);
    }

    private void handleEvent(AnnotationEventListenerAdapter annotationEventListenerAdapter, EventMessage<?> event)
    {
        try
        {
            annotationEventListenerAdapter.handle(event);
        } catch (Exception e1)
        {
            throw new RuntimeException(e1);

        }
    }
}
