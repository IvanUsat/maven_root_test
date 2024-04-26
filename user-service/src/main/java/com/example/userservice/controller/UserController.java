package com.example.userservice.controller;


import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import org.apache.kafka.common.requests.ApiVersionsResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;


    @PostMapping(value = "/add")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User userCreated = userService.createUser(user);
        return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
    }

    @GetMapping(value = "/findAll")
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers().stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findUserById(id);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return ResponseEntity.ok().body(userDto);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id, @RequestBody UserDto userDto) {
        User userRequest = modelMapper.map(userDto, User.class);
        User user = userService.updateUser(id, userRequest);
        UserDto userResponse = modelMapper.map(user, UserDto.class);
        return ResponseEntity.ok().body(userResponse);
    }

    @DeleteMapping(value = "/delete/{name}")
    public ResponseEntity<Void> delete(@PathVariable(name = "name") String name) {
        userService.deleteUser(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
