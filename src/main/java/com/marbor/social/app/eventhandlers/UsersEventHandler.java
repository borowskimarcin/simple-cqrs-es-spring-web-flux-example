package com.marbor.social.app.eventhandlers;

import com.marbor.social.app.domain.User;
import com.marbor.social.app.events.user.FollowerAddedEvent;
import com.marbor.social.app.events.user.UserCreatedEvent;
import com.marbor.social.app.repositories.UserRepository;
import org.axonframework.eventhandling.EventHandler;


public class UsersEventHandler
{
    @EventHandler
    public void handle(UserCreatedEvent event) {
        UserRepository.getRepository().save(new User(event.getName(), event.getId()));
        System.out.println("User created: " + event.getName() + " (" + event.getId() + ")");
    }

    @EventHandler
    public void handle(FollowerAddedEvent event) {
        UserRepository.getRepository().findById(event.getUserId())
                .flatMap(user -> UserRepository.getRepository()
                        .findById(event.getFollowerId())
                        .map(follower -> user.getFollowers().add(follower.getId())))
                .subscribe();

        System.out.println("Follower: " + event.getFollowerId() + " added to: " + event.getUserId());
    }
}