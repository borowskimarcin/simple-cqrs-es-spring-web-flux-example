package com.marbor.social.app.eventhandlers;

import com.marbor.social.app.domain.User;
import com.marbor.social.app.events.SubscribeEvent;
import com.marbor.social.app.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Created by marcin on 08.07.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class UsersEventHandlerTest
{
    private UsersEventHandler usersEventHandler = new UsersEventHandler();

    @Mock
    private UserRepository repository;

    @Before
    public void setUp()
    {
        usersEventHandler.repository = repository;
    }

    @Test
    public void testEventHandlerWithSubscribeEvent()
    {
        // given
        String userId = UUID.randomUUID().toString();
        String followedId = UUID.randomUUID().toString();
        User user = new User("name", userId);
        User followedUser = new User("name2", followedId);
        SubscribeEvent subscribeEvent = new SubscribeEvent(userId, followedId);
        Mockito.when(repository.findById(userId)).thenReturn(Mono.just(user));
        Mockito.when(repository.findById(followedId)).thenReturn(Mono.just(followedUser));

        // when
        usersEventHandler.handle(subscribeEvent);

        // then
        Mockito.verify(repository,Mockito.times(1)).findById(userId);
        Mockito.verify(repository,Mockito.times(1)).findById(followedId);
        assertEquals(user.getFollowedIds().get(0), followedId);
        assertEquals(followedUser.getFollowersIds().get(0), userId);
    }
}