package com.example.UserService.service.impl;


import com.example.UserService.dto.request.RegisterReq;
import com.example.UserService.entity.User;
import com.example.UserService.mapper.impl.UserMapper;
import com.example.UserService.repository.UserRepository;
import com.example.UserService.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

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



}
