package com.marbor.social.app.repositories;

import com.marbor.social.app.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by marcin on 08.07.17.
 */
public class UserRepository implements Repository<User>
{
    private static final UserRepository USER_REPOSITORY = new UserRepository();

    private final LinkedHashMap<String, User> localRepo = new LinkedHashMap<>(128);

    private UserRepository(){}

    @Override
    public void save(User user)
    {
        if (!contains(user.getName()))
        {
            localRepo.put(user.getId(), user);
        }
    }

    @Override
    public void delete(User entity)
    {
        localRepo.remove(entity.getId());
    }

    @Override
    public Flux<User> findAll()
    {
        List<User> users = localRepo.values().stream().collect(Collectors.toList());
        if (users.isEmpty())
        {
            return Flux.empty();
        }

        return Flux.fromStream(users.stream());
    }

    @Override
    public Mono<User> findById(String id)
    {
        return Mono.justOrEmpty(localRepo.get(id));
    }

    public Mono<Boolean> containsUserWithName(String name)
    {
        return Mono.just(contains(name));
    }

    private boolean contains(String name)
    {
        return localRepo.values().stream()
                .anyMatch(user -> user.getName().equals(name));
    }
    public static UserRepository getRepository()
    {
        return USER_REPOSITORY;
    }
}
