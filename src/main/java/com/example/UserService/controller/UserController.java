package com.example.UserService.controller;

import com.example.UserService.dto.request.ChangePasswordReq;
import com.example.UserService.dto.request.RegisterReq;
import com.example.UserService.dto.request.UpdateUserReq;
import com.example.UserService.dto.response.MessagesResponse;
import com.example.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getUserInfor")
    public RegisterReq getUserInFor() {
        return userService.getCurrentUser();
    }
    @PutMapping
    public MessagesResponse updateUser(@RequestBody UpdateUserReq updateUserReq){
        MessagesResponse ms = new MessagesResponse();
        try {
            userService.updateUser(updateUserReq);
        }
        catch (Exception exception) {
            ms.code=5000;
            ms.message= "Internal Server Error!";
        }
        return ms;
    }
    @PostMapping("/changePassword")
    public MessagesResponse changePassword(@RequestBody ChangePasswordReq changePasswordReq) {
        MessagesResponse ms = new MessagesResponse();
        try {
             userService.changePassword(changePasswordReq);
        }
        catch (Exception ex) {
            ms.code=500;
            ms.message="Change Password failed!";
        }
        return  ms;
    }
}
