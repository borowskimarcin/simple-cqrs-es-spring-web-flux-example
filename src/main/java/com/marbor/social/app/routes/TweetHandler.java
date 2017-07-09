package com.marbor.social.app.routes;

import com.marbor.social.app.commands.CreateTweetCommand;
import com.marbor.social.app.domain.Tweet;
import com.marbor.social.app.query.TweetsQueryService;
import com.marbor.social.app.repositories.UserRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.marbor.social.app.routes.RestMessages.TWEET_MESSAGE_TOO_LONG;
import static com.marbor.social.app.routes.RestMessages.USER_ID_NOT_EXISTS;
import static com.marbor.social.app.utils.Utils.toMono;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * Created by marcin on 08.07.17.
 */
public class TweetHandler
{
    private final TweetsQueryService tweetsQueryService = new TweetsQueryService();
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

        return tweetsQueryService.findAllTweets()
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

        return tweetsQueryService.findTweetsForWall(userId)
                .flatMap(tweets ->
                        {
                            if (tweets.isEmpty())
                            {
                                return notFound;
                            }

                            return ServerResponse.ok()
                                    .contentType(APPLICATION_JSON)
                                    .body(fromObject(tweets));
                        }
                );
    }

    Mono<ServerResponse> getTweetsForTimeLine(ServerRequest serverRequest)
    {
        String userId = serverRequest.pathVariable("id");

        Mono<ServerResponse> notFound = ServerResponse
                .notFound()
                .header(Header.ERROR.message(), RestMessages.TWEETS_NOT_FOUND.message())
                .build();

        return tweetsQueryService.findTweetsForTimeLine(userId)
                .flatMap(tweets ->
                        {
                            if (tweets.isEmpty())
                            {
                                return notFound;
                            }

                            return ServerResponse.ok()
                                    .contentType(APPLICATION_JSON)
                                    .body(fromObject(tweets));
                        }
                );
    }
}
