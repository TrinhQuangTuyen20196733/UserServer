package com.example.UserService.service;


import com.example.UserService.dto.request.ChangePasswordReq;
import com.example.UserService.dto.request.RegisterReq;

import com.example.UserService.dto.request.UpdateUserReq;
import com.example.UserService.dto.response.MessagesResponse;
import com.example.UserService.entity.User;

import java.util.Optional;

public interface UserService {
    User create(RegisterReq registerReq);




    void deleteUser(long id);

    Optional<User> findByEmail(String email);



    User findById(long Id);

    RegisterReq getCurrentUser();

    void updateUser(UpdateUserReq updateUserReq);

    void changePassword(ChangePasswordReq changePasswordReq);
}