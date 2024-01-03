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
import java.util.stream.Collectors;

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
        String role = entity.getRole().getCode();
        String gender = entity.getGender().getValue();
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<User, RegisterReq> typeMap =  modelMapper.createTypeMap(User.class,RegisterReq.class);
        typeMap.addMappings(mapping->mapping.map(src->role,RegisterReq::setRole));

        typeMap.addMappings(mapping->mapping.map(src->gender,RegisterReq::setGender));
        RegisterReq userRes =  modelMapper.map(entity,RegisterReq.class);

        return  userRes;
    }

    @Override
    public List<RegisterReq> toDTOList(List<User> entityList) {
        return  entityList.stream()
                .map(user -> toDTO(user))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> toEntityList(List<RegisterReq> dtoList) {
        return null;
    }
}
