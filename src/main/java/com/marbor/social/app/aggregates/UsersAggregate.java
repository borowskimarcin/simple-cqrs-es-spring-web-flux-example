package com.marbor.social.app.aggregates;


import com.marbor.social.app.commands.user.AddFollowerCommand;
import com.marbor.social.app.commands.user.CreateUserCommand;
import com.marbor.social.app.events.user.FollowerAddedEvent;
import com.marbor.social.app.events.user.UserCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventhandling.EventHandler;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;


public class UsersAggregate
{
    @AggregateIdentifier
    private String id;

    public UsersAggregate() {
    }

    @CommandHandler
    public UsersAggregate(CreateUserCommand command) {
        apply(new UserCreatedEvent(command.getId(), command.getName()));
    }

    @CommandHandler
    public void addFollower(AddFollowerCommand command) {
        apply(new FollowerAddedEvent(command.getId(), command.getFollowerId()));
    }

    @EventHandler
    public void on(UserCreatedEvent event) {
        this.id = event.getId();
    }
}