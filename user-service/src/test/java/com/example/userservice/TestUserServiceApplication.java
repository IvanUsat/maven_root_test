package com.example.userservice;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertAll;



@ExtendWith(MockitoExtension.class)
public class TestUserServiceApplication {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private User user;

    @BeforeEach
    public void setup(){
        user = User.builder()
                .firstName("Petr")
                .lastName("Petrov")
                .email("petrov@mail.corm")
                .password("12345")
                .build();
    }
    @Test
    public void testCreateUser(){
        when(repository.save(Mockito.any(User.class))).thenReturn(user);
        User savedUser = service.createUser(user);
        Assertions.assertThat(savedUser).isNotNull();
    }

    @Test
    public void testFindAllUsers(){
        User user2 = User.builder()
                .firstName("Petr")
                .lastName("Petrov")
                .email("petrov@mail.corm")
                .password("12345")
                .build();
        List<User> list = Arrays.asList(user, user2);
        when(repository.findAll()).thenReturn(list);
        List<User> result = service.findAllUsers();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void testFindUserById(){
        long userId = 1;
        when(repository.findById(userId)).thenReturn(Optional.ofNullable(user));
        User result = service.findUserById(userId);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void testDeleteUserByName(){
        String name = "Petr";
        when(repository.findByFirstName(name)).thenReturn(user);
        doNothing().when(repository).delete(user);
        assertAll(()-> service.deleteUser(name));
    }

    @Test
    public void testUpdateUser(){
        long userId = 1;
        User userRequest = User.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivanov@mail.corm")
                .password("123456")
                .build();
        when(repository.findById(userId)).thenReturn(Optional.ofNullable(user));
        when(repository.save(user)).thenReturn(user);
        User updateReturn = service.updateUser(userId, userRequest);
        Assertions.assertThat(updateReturn).isNotNull();
    }

}

