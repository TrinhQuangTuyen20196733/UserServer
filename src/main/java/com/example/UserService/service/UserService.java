package com.example.UserService.service;


import com.example.UserService.dto.request.RegisterReq;

import com.example.UserService.entity.User;

import java.util.Optional;

public interface UserService {
    User create(RegisterReq registerReq);




    void deleteUser(long id);

    Optional<User> findByEmail(String email);



    User findById(long Id);

}