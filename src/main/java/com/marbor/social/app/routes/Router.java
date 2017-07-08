package com.marbor.social.app.routes;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Created by marcin on 07.07.17.
 */
public class Router
{
    private final UserHandler userHandler;
    private final TweetHandler tweetHandler;

    public Router(CommandGateway commandGateway)
    {
        this.userHandler = new UserHandler(commandGateway);
        this.tweetHandler = new TweetHandler(commandGateway);
    }

    public RouterFunction<ServerResponse> routingFunction()
    {
        return route(POST(Routes.CREATE_USER.pattern()), userHandler::createUser)
                .andRoute(GET(Routes.GET_USERS.pattern()), userHandler::getUsers)
                .andRoute(GET(Routes.GET_USER.pattern()), userHandler::getUser)
                .andRoute(PATCH(Routes.SUBSCRIBE.pattern()), userHandler::subscribe)
                .andRoute(GET(Routes.GET_SUBSCRIPTIONS.pattern()), userHandler::getSubscriptions)
                .andRoute(POST(Routes.CREATE_TWEET.pattern()), tweetHandler::createTweet)
                .andRoute(GET(Routes.GET_TWEETS.pattern()),tweetHandler::getTweets);
    }
}

