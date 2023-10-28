package com.example.UserService.mapper.impl;

import com.example.UserService.dto.request.RegisterReq;
import com.example.UserService.entity.Role;
import com.example.UserService.entity.User;
import com.example.UserService.enum_constant.Gender;
import com.example.UserService.mapper.Mapper;
import com.example.UserService.repository.RoleRepository;

import com.example.UserService.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<User, RegisterReq> {

    private final RoleRepository roleRepository;

    @Override
    public User toEntity(RegisterReq dto) {

        Gender gender = dto.getGender().equals("MALE") ? Gender.MALE : Gender.FEMALE;
        Role role = roleRepository.findByCode(dto.getRole()).orElse(null);
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<RegisterReq, User> typeMap =  modelMapper.createTypeMap(RegisterReq.class,User.class);
        typeMap.addMappings(mapping->mapping.map(src->role,User::setRole));

        typeMap.addMappings(mapping->mapping.map(src->gender,User::setGender));
        return modelMapper.map(dto,User.class);
    }

    @Override
    public RegisterReq toDTO(User entity) {
        return null;
    }

    @Override
    public List<RegisterReq> toDTOList(List<User> entityList) {
        return null;
    }

    @Override
    public List<User> toEntityList(List<RegisterReq> dtoList) {
        return null;
    }
}
