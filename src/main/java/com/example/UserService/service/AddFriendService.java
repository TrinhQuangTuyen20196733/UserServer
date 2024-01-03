package com.example.UserService.service;

import com.example.UserService.dto.request.RegisterReq;
import com.example.UserService.entity.User;

import java.util.List;

public interface AddFriendService {
     void addFriend(long userId);

    void accept(long userId);

    void cancel(long userId);

    List<RegisterReq> getPending(long userId);
}
