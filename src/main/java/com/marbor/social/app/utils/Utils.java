package com.marbor.social.app.utils;

import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 * Created by marcin on 08.07.17.
 */
public class Utils
{
    public static <T> Mono<T> toMono(CompletableFuture<T> future)
    {
        return Mono.create(subscriber ->
                future.whenComplete((result, error) ->
                {
                    if (error != null)
                    {
                        subscriber.error(error);
                    } else
                    {
                        subscriber.success(result);
                    }
                }));
    }
}
