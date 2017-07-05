package com.marbor.social.app.repositories;

import com.marbor.social.app.domain.DomainObject;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by marcin on 08.07.17.
 */
public interface Repository<T extends DomainObject>
{
    /**
     *
     * @param entity
     */
    void save(T entity);

    /**
     *
     * @param entity
     * @return returns deleted Object or empty result
     */
    void delete(T entity);

    /**
     *
     * @return returns all stored objects
     */
    Mono<List<T>> findAll();

    /**
     *
     * @param id
     * @return returns given object or empty result
     */
    Mono<T> findById(String id);
}
