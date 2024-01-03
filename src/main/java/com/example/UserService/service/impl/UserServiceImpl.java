package com.example.UserService.service.impl;


import com.example.UserService.dto.request.ChangePasswordReq;
import com.example.UserService.dto.request.RegisterReq;
import com.example.UserService.dto.request.UpdateUserReq;
import com.example.UserService.entity.User;
import com.example.UserService.enum_constant.Gender;
import com.example.UserService.exception.BusinessLogicException;
import com.example.UserService.mapper.impl.UserMapper;
import com.example.UserService.repository.UserRepository;
import com.example.UserService.service.KeycloakService;
import com.example.UserService.service.UserService;
import com.example.UserService.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeycloakService keycloakService;

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Override
    public User create(RegisterReq registerReq) {
        User user = userRepository.save(userMapper.toEntity(registerReq));
        return user;
    }


    @Override
    public void deleteUser(long id) {

        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);


    }


    @Override
    public User findById(long Id) {
        var userOptional = userRepository.findById(Id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        ;
        return null;
    }

    @Override
    public RegisterReq getCurrentUser() {
        String email = EmailUtils.getCurrentUser();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException());

        return userMapper.toDTO(user);


    }

    @Override
    public void updateUser(UpdateUserReq updateUserReq) {
        userRepository.updateUser(updateUserReq.getId(), updateUserReq.getName(), updateUserReq.getPhoneNumber(), updateUserReq.getAddress(), updateUserReq.getAge(), Gender.valueOf(updateUserReq.getGender()));
    }

    @Override
    public void changePassword(ChangePasswordReq changePasswordReq) {
        String email = EmailUtils.getCurrentUser();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException());
        if (user.getPassword().equals(changePasswordReq.getOldPassword())) {
            user.setPassword(changePasswordReq.getNewPassword());
            userRepository.save(user);
            keycloakService.changePassword(changePasswordReq);
        }
    }


}
