package com.marbor.social.app.routes;

import com.marbor.social.app.commands.user.CreateUserCommand;
import com.marbor.social.app.commands.user.SubscribeCommand;
import com.marbor.social.app.domain.User;
import com.marbor.social.app.repositories.UserRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.marbor.social.app.routes.RestMessages.*;
import static com.marbor.social.app.utils.Utils.toMono;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * Created by marcin on 07.07.17.
 */
class UserHandler
{
    private final CommandGateway commandGateway;

    UserHandler(CommandGateway commandGateway)
    {
        this.commandGateway = commandGateway;
    }


    Mono<ServerResponse> createUser(ServerRequest request)
    {
        Mono<ServerResponse> badRequest = ServerResponse.badRequest()
                .body(fromObject(USER_ALREADY_EXISTS.message()));

        return request.body(BodyExtractors.toMono(User.class))
                .flatMap(user ->
                        UserRepository.getRepository()
                                .containsUserWithName(user.getName())
                                .flatMap(userExists ->
                                {
                                    if (userExists)
                                    {
                                        return badRequest;
                                    }
                                    else
                                    {
                                        return toMono(commandGateway.send(new CreateUserCommand(user.getId(), user.getName())))
                                                .flatMap(result -> ServerResponse.ok()
                                                        .contentType(APPLICATION_JSON).body(fromObject(user)));
                                    }
                                }));

    }

    Mono<ServerResponse> getUser(ServerRequest request)
    {
        String id = request.pathVariable("id");

        Mono<ServerResponse> notFound = ServerResponse
                .notFound()
                .header("Message", RestMessages.USER_NOT_FOUND.message())
                .build();

        return UserRepository.getRepository()
                .findById(id)
                .flatMap(u ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(fromObject(u)))
                .switchIfEmpty(notFound);
    }

    Mono<ServerResponse> getUsers(ServerRequest request)
    {
        Mono<ServerResponse> notFound = ServerResponse
                .notFound()
                .header("Message", USERS_NOT_FOUND.message())
                .build();

        return UserRepository.getRepository()
                .findAll()
                .collectList()
                .flatMap(u ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(fromObject(u)))
                .switchIfEmpty(notFound);

    }

    Mono<ServerResponse> subscribe(ServerRequest request)
    {
        String id = request.pathVariable("id");
        String followedId = request.pathVariable("followedId");

        //TODO follower already exists scenario
        return toMono(commandGateway.send(new SubscribeCommand(id, followedId)))
                .flatMap((result) -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromObject("User: " + id + " subscribed: " + followedId)));
    }

    Mono<ServerResponse> getSubscriptions(ServerRequest request)
    {
        Mono<ServerResponse> notFound = ServerResponse
                .notFound()
                .header("Message", SUBSCRIPTION_NOT_FOUND.message())
                .build();

        return UserRepository.getRepository()
                .findById(request.pathVariable("id"))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromObject(user.getFollowedIds())))
                .switchIfEmpty(notFound);
    }


}
