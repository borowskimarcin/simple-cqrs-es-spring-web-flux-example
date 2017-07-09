package com.marbor.social.app.query;

import com.marbor.social.app.domain.Tweet;
import com.marbor.social.app.repositories.TweetRepository;
import reactor.core.publisher.Mono;

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
        return TweetRepository.getRepository().findAll().collectList();
    }

    public Mono<List<Tweet>> findTweetsForWall(String userId)
    {
        return findAllTweets()
                .map(tweets -> reverse(getWallTweets(userId, tweets)));
    }

    public Mono<List<Tweet>> findTweetsForTimeLine(String userId)
    {
        return findAllTweets()
                .map(tweets -> reverse(getTimeLineTweets(userId, tweets)));
    }


    private List<Tweet> reverse(List<Tweet> wallTweets)
    {
        Collections.reverse(wallTweets);
        return wallTweets;
    }

    private List<Tweet> getWallTweets(String userId, List<Tweet> tweets)
    {
        return getUserTweets(userId, tweets).stream()
                .filter(userTweet -> userTweet.getOwnerId().equals(userTweet.getAuthorId()))
                .collect(Collectors.toList());
    }

    private List<Tweet> getTimeLineTweets(String userId, List<Tweet> tweets)
    {
        return getUserTweets(userId, tweets).stream()
                .filter(userTweet -> !userTweet.getOwnerId().equals(userTweet.getAuthorId()))
                .collect(Collectors.toList());
    }

    private List<Tweet> getUserTweets(String userId, List<Tweet> tweets)
    {
        return tweets.stream()
                .filter(tweet -> tweet.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }
}
