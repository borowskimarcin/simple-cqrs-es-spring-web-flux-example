package com.marbor.social.app.query;

import com.marbor.social.app.domain.User;
import com.marbor.social.app.repositories.UserRepository;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by marcin on 09.07.17.
 */
public class UserQueryService
{
    public Mono<List<User>> findAllUsers()
    {
        return UserRepository.getRepository()
                .findAll()
                .collectList();
    }

    public Mono<User> findUser(String userId)
    {
        return UserRepository.getRepository()
                .findById(userId);
    }

    public Mono<List<String>> findSubscriptions(String userId)
    {
        return findUser(userId)
                .map(User::getFollowedIds);
    }
}
