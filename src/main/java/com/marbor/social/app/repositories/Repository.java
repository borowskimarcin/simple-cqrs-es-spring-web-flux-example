package com.marbor.social.app.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by marcin on 08.07.17.
 */
public interface Repository<T>
{
    /**
     *
     * @param entity
     */
    void save(T entity);

    /**
     *
     * @return returns all stored objects
     */
    Flux<T> findAll();

    /**
     *
     * @param id
     * @return returns given object or empty result
     */
    Mono<T> findById(String id);
}
