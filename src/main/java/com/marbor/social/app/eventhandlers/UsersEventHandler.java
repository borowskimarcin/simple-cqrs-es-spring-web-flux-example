package com.marbor.social.app.eventhandlers;

import com.marbor.social.app.domain.User;
import com.marbor.social.app.events.user.SubscribeEvent;
import com.marbor.social.app.events.user.UserCreatedEvent;
import com.marbor.social.app.repositories.UserRepository;
import org.axonframework.eventhandling.EventHandler;


public class UsersEventHandler
{
    // visibility for tests purpose
    UserRepository repository = UserRepository.getRepository();

    @EventHandler
    public void handle(UserCreatedEvent event) {
        repository.save(new User(event.getName(), event.getId()));
        System.out.println("User created: " + event.getName() + " (" + event.getId() + ")");
    }

    @EventHandler
    public void handle(SubscribeEvent event) {
        repository.findById(event.getUserId())
                .flatMap(user -> repository
                        .findById(event.getFollowedId())
                        .map(followedUser -> syncSubscription(user, followedUser)))
                .subscribe();

        System.out.println("Follower: " + event.getFollowedId() + " added to: " + event.getUserId());
    }

    private boolean syncSubscription(User user, User followedUser)
    {
        user.getFollowedIds().add(followedUser.getId());
        followedUser.getFollowersIds().add(user.getId());
        return true;
    }
}