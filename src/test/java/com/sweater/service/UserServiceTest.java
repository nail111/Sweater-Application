package com.sweater.service;

import com.sweater.model.Role;
import com.sweater.model.User;
import com.sweater.repo.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MailSenderService mailSenderService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void addUser() {
        User user = new User();

        user.setEmail("some@mail.ru");

        boolean isUserCreated = userService.addUser(user);

        assertTrue(isUserCreated);
        assertNotNull(user.getActivationCode());
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        verify(userRepo, times(1)).save(user);
        verify(mailSenderService, times(1)).send(
                ArgumentMatchers.eq(user.getEmail()),
                anyString(),
                anyString()
        );
    }

    @Test
    void addUserFailedTest() {
        User user = new User();
        user.setUsername("John");

        doReturn(new User())
                .when(userRepo)
                .findByUsername("John");

        boolean isUserCreated = userService.addUser(user);
        assertFalse(isUserCreated);

        verify(userRepo, times(0)).save(any(User.class));
        verify(mailSenderService, times(0))
                .send(
                        anyString(),
                        anyString(),
                        anyString()
                );
    }

    @Test
    void activateUser() {
        User user = new User();

        user.setActivationCode("bingo!");

        doReturn(user)
                .when(userRepo)
                .findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");
        assertTrue(isUserActivated);
        assertNull(user.getActivationCode());

        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void activateUserFailTest() {
        boolean isUserActivated = userService.activateUser("activate me");

        assertFalse(isUserActivated);
        verify(userRepo, times(0)).save(any(User.class));
    }
}