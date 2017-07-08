package com.marbor.social.app.repositories;

import com.marbor.social.app.domain.Tweet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by marcin on 08.07.17.
 */
public class TweetRepository implements Repository<Tweet>
{
    private static final TweetRepository TWEET_REPOSITORY = new TweetRepository();
    private final HashMap<String, Tweet> localRepo = new HashMap<>(128);

    @Override
    public void save(Tweet entity)
    {
        localRepo.put(entity.getId(), entity);
    }

    @Override
    public void delete(Tweet entity)
    {
        localRepo.remove(entity.getId());
    }

    @Override
    public Flux<Tweet> findAll()
    {
        List<Tweet> tweets = localRepo.values().stream().collect(Collectors.toList());
        if (tweets.isEmpty())
        {
            return Flux.empty();
        }

        return Flux.fromStream(tweets.stream());
    }

    @Override
    public Mono<Tweet> findById(String id)
    {
        return Mono.justOrEmpty(localRepo.get(id));
    }

    public static TweetRepository getRepository()
    {
        return TWEET_REPOSITORY;
    }
}
