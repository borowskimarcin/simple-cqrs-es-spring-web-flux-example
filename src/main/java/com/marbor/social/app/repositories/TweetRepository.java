package com.marbor.social.app.repositories;

import com.marbor.social.app.domain.Tweet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

/**
 * Created by marcin on 08.07.17.
 */
public class TweetRepository implements Repository<List<Tweet>> {
    private static final TweetRepository TWEET_REPOSITORY = new TweetRepository();
    private final HashMap<String, List<Tweet>> localRepo = new HashMap<>(128);

    @Override
    public void save(List<Tweet> entity) {
        entity = new ArrayList<>(entity);
        if (!entity.isEmpty()) {
            String ownerId = entity.get(0).getOwnerId();
            List<Tweet> beforeTweets = Optional.ofNullable(localRepo.get(ownerId))
                    .orElse(Collections.emptyList());

            localRepo.put(ownerId, Stream.concat(beforeTweets.stream(), entity.stream())
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public Flux<List<Tweet>> findAll() {
        List<List<Tweet>> tweets = new ArrayList<>(localRepo.values());
        if (tweets.isEmpty()) {
            return Flux.empty();
        }

        return Flux.fromStream(tweets.stream());
    }

    @Override
    public Mono<List<Tweet>> findById(String id) {
        return Mono.justOrEmpty(localRepo.get(id));
    }

    public static TweetRepository getRepository() {
        return TWEET_REPOSITORY;
    }
}
