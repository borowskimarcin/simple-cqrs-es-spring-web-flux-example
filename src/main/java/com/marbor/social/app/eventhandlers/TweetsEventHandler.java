package com.marbor.social.app.eventhandlers;

import com.marbor.social.app.domain.Tweet;
import com.marbor.social.app.domain.User;
import com.marbor.social.app.events.TweetCreatedEvent;
import com.marbor.social.app.repositories.TweetRepository;
import com.marbor.social.app.repositories.UserRepository;
import org.axonframework.eventhandling.EventHandler;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;


public class TweetsEventHandler
{
    //visibility for tests
    TweetRepository tweetRepository = TweetRepository.getRepository();
    //visibility for tests
    UserRepository userRepository = UserRepository.getRepository();

    @EventHandler
    public void handle(TweetCreatedEvent event)
    {
        userRepository
                .findById(event.getAuthorId())
                .map(author -> saveTweets(event, author))
                .subscribe();
        System.out.println("Message received: " + event.getMessage() + " (" + event.getId() + ")");
    }

    private Mono<Object> saveTweets(TweetCreatedEvent event, User author)
    {
        saveTweetForAuthor(event);
        saveTweetsForFollowers(event, author);
        return Mono.empty();
    }

    private void saveTweetsForFollowers(TweetCreatedEvent event, User author)
    {
        author.getFollowersIds()
                .forEach(followerId -> tweetRepository.save(
                        singletonList(new Tweet(event.getId() + followerId,
                                event.getMessage(),
                                followerId,
                                event.getAuthorId()))));
    }

    private void saveTweetForAuthor(TweetCreatedEvent event)
    {
        tweetRepository.save(singletonList(new Tweet(event.getId() + event.getAuthorId(),
                event.getMessage(),
                event.getAuthorId(),
                event.getAuthorId())));
    }

}