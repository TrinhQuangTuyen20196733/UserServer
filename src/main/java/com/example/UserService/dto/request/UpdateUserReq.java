package com.example.UserService.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserReq {

    private int id;

    private String name;

    private String phoneNumber;

    private String address;

    private int age;

    private String gender;
}
