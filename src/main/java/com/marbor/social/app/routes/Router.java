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

    public Router(CommandGateway commandGateway)
    {
        this.userHandler = new UserHandler(commandGateway);
    }

    public RouterFunction<ServerResponse> routingFunction()
    {
        return route(POST(Routes.CREATE_USER.pattern()), userHandler::createUser)
                .andRoute(GET(Routes.GET_USERS.pattern()), userHandler::getUsers)
                .andRoute(GET(Routes.GET_USER.pattern()), userHandler::getUser)
                .andRoute(PATCH(Routes.ADD_FOLLOWER.pattern()), userHandler::addFollower)
                .andRoute(GET(Routes.GET_FOLLOWERS.pattern()), userHandler::getFollowers);
    }
}

