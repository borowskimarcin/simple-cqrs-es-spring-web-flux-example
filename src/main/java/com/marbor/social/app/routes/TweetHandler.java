package com.marbor.social.app.routes;

import com.marbor.social.app.commands.CreateTweetCommand;
import com.marbor.social.app.domain.Tweet;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
        Mono<ServerResponse> badRequest = ServerResponse.badRequest()
                .body(fromObject(TWEET_MESSAGE_TOO_LONG.message()));
        //here should be validation part

        return request.body(BodyExtractors.toMono(Tweet.class))
                .filter(this::validateSize)
                .flatMap(tweet -> toMono(commandGateway.send(
                        new CreateTweetCommand(tweet.getId(),
                                tweet.getMessage(),
                                tweet.getAuthorId()
                        )))
                        .flatMap(result -> ServerResponse.ok()
                                .contentType(APPLICATION_JSON).body(fromObject(tweet))))
                .switchIfEmpty(badRequest);
    }

    private boolean validateSize(Tweet tweet)
    {
        return tweet.getMessage().length() <= 140;
    }
}
