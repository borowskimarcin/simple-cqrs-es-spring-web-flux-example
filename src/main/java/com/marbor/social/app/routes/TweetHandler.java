package com.marbor.social.app.routes;

import com.marbor.social.app.commands.CreateTweetCommand;
import com.marbor.social.app.domain.Tweet;
import com.marbor.social.app.repositories.TweetRepository;
import com.marbor.social.app.repositories.UserRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.marbor.social.app.routes.RestMessages.USER_ID_NOT_EXISTS;
import static com.marbor.social.app.routes.RestMessages.TWEET_MESSAGE_TOO_LONG;
import static com.marbor.social.app.utils.Utils.toMono;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * Created by marcin on 08.07.17.
 */
public class TweetHandler
{
    private final CommandGateway commandGateway;

    public TweetHandler(CommandGateway commandGateway)
    {
        this.commandGateway = commandGateway;
    }

    Mono<ServerResponse> createTweet(ServerRequest request)
    {
        Mono<ServerResponse> badRequestMessageTooLong = ServerResponse.badRequest()
                .header(Header.ERROR.message(), TWEET_MESSAGE_TOO_LONG.message())
                .build();
        Mono<ServerResponse> badRequestAuthorDoesNotExists = ServerResponse.badRequest()
                .header(Header.ERROR.message(), USER_ID_NOT_EXISTS.message())
                .build();

        String authorId = request.pathVariable("id");


        return UserRepository.getRepository().findById(authorId)
                .flatMap(user -> request.body(BodyExtractors.toMono(Tweet.class))
                        .filter(this::validateSize)
                        .flatMap(tweet -> toMono(commandGateway.send(
                                new CreateTweetCommand(tweet.getId(),
                                        tweet.getMessage(),
                                        authorId
                                )))
                                .flatMap(result -> ServerResponse.ok()
                                        .contentType(APPLICATION_JSON).body(fromObject(fillTweet(tweet, authorId)))))
                        .switchIfEmpty(badRequestMessageTooLong))
                .switchIfEmpty(badRequestAuthorDoesNotExists);
    }

    private Tweet fillTweet(Tweet tweet, String authorId)
    {
        return new Tweet(tweet.getId(), tweet.getMessage(), authorId, authorId);
    }

    private boolean validateSize(Tweet tweet)
    {
        return tweet.getMessage().length() <= 140;
    }

    Mono<ServerResponse> getTweets(ServerRequest serverRequest)
    {
        Mono<ServerResponse> notFound = ServerResponse
                .notFound()
                .header(Header.ERROR.message(), RestMessages.TWEETS_NOT_FOUND.message())
                .build();

        return TweetRepository.getRepository().findAll().collectList()
                .flatMap(tweets ->
                {
                    if (tweets.isEmpty())
                    {
                        return notFound;
                    }

                    return ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .body(fromObject(tweets));
                });
    }

    Mono<ServerResponse> getTweetsForWall(ServerRequest serverRequest)
    {
        String userId = serverRequest.pathVariable("id");

        Mono<ServerResponse> notFound = ServerResponse
                .notFound()
                .header(Header.ERROR.message(), RestMessages.TWEETS_NOT_FOUND.message())
                .build();

        return TweetRepository.getRepository().findAll().collectList()
                .flatMap(tweets ->
                        {
                            if (tweets.isEmpty())
                            {
                                return notFound;
                            }

                            return ServerResponse.ok()
                                    .contentType(APPLICATION_JSON)
                                    .body(fromObject(reverse(getWallTweets(userId, tweets))));
                        }
                );
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

    Mono<ServerResponse> getTweetsForTimeLine(ServerRequest serverRequest)
    {
        String userId = serverRequest.pathVariable("id");

        Mono<ServerResponse> notFound = ServerResponse
                .notFound()
                .header(Header.ERROR.message(), RestMessages.TWEETS_NOT_FOUND.message())
                .build();

        return TweetRepository.getRepository().findAll().collectList()
                .flatMap(tweets ->
                        {
                            if (tweets.isEmpty())
                            {
                                return notFound;
                            }

                            return ServerResponse.ok()
                                    .contentType(APPLICATION_JSON)
                                    .body(fromObject(reverse(getTimeLineTweets(userId, tweets))));
                        }
                );
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
