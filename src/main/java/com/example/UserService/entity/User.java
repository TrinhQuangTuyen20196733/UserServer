package com.example.UserService.entity;

import com.example.UserService.enum_constant.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(indexes = {
        @Index(name = "name_index",columnList = "name")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends  BaseEntity {

    @Email(message = "Email isn't valid")
    @Column(unique = true,nullable = false)
    private  String email;

    private String password;

    @Column(nullable = false)
    private  String name;

    private String address;

    private  String phoneNumber;


    private  String avatarLocation;

    @Min(5)
    @Max(100)
    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_code")
    private Role role;

    private  boolean isOauth2;

}
