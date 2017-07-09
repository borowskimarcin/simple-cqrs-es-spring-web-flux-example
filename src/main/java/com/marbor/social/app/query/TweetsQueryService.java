package com.marbor.social.app.query;

import com.marbor.social.app.domain.Tweet;
import com.marbor.social.app.repositories.TweetRepository;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by marcin on 09.07.17.
 */
public class TweetsQueryService
{
    public Mono<List<Tweet>> findAllTweets()
    {
        return TweetRepository.getRepository()
                .findAll()
                .collectList()
                .map(input -> input.stream()
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList()));
    }

    public Mono<List<Tweet>> findTweetsForWall(String userId)
    {
        return findUserTweets(userId)
                .map(tweets -> reverse(getWallTweets(tweets)));
    }

    public Mono<List<Tweet>> findTweetsForTimeLine(String userId)
    {
        return findUserTweets(userId)
                .map(tweets -> reverse(getTimeLineTweets(tweets)));
    }

    private Mono<List<Tweet>> findUserTweets(String userId) {
        return TweetRepository.getRepository()
                .findById(userId);
    }

    private List<Tweet> reverse(List<Tweet> wallTweets)
    {
        Collections.reverse(wallTweets);
        return wallTweets;
    }

    private List<Tweet> getWallTweets(List<Tweet> tweets)
    {
        return tweets.stream()
                .filter(userTweet -> userTweet.getOwnerId().equals(userTweet.getAuthorId()))
                .collect(Collectors.toList());
    }

    private List<Tweet> getTimeLineTweets(List<Tweet> tweets)
    {
        return tweets.stream()
                .filter(userTweet -> !userTweet.getOwnerId().equals(userTweet.getAuthorId()))
                .collect(Collectors.toList());
    }
}
