package com.example.userservice.service;


import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserService {


    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("User with id " + id + " has not found");
        }
    }

    public User updateUser(Long id, User userRequest) {
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        return userRepository.save(user);
    }

    public void deleteUser(String name) {
        userRepository.delete(userRepository.findByFirstName(name));

    }
}
