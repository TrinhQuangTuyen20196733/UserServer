package com.example.UserService.controller;

import com.example.UserService.dto.request.LoginRequest;
import com.example.UserService.dto.request.RefreshTokenRq;
import com.example.UserService.dto.response.AuthenticationResponse;
import com.example.UserService.entity.TokenObj;
import com.example.UserService.exception.RefreshTokenFailedException;
import com.example.UserService.exception.UnAuthorizedException;
import com.example.UserService.service.KeycloakService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Value("${refreshToken.expiredTime}")
    private  int EXPIRED_TIME;

    private final RedisTemplate<String, Object> redisTemplate;

    private  final KeycloakService keycloakService;

    @PostMapping("/login")
    public AuthenticationResponse Login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            String accessToken = keycloakService.getAccessToken(loginRequest.getEmail(), loginRequest.getPassword());
            String refreshToken = UUID.randomUUID().toString();
            TokenObj tokenObj = new TokenObj();
            tokenObj.setEmail(loginRequest.getEmail());
            tokenObj.setPassword(loginRequest.getPassword());
            tokenObj.setCreated(LocalDateTime.now());
            redisTemplate.opsForHash().put(refreshToken,refreshToken.hashCode(), tokenObj);
            redisTemplate.expire(refreshToken, 300, TimeUnit.SECONDS);
            return new AuthenticationResponse(accessToken,refreshToken);
        }
        catch (Exception e) {
            throw  new UnAuthorizedException(e.getMessage());
        }
}

    @PostMapping("/refreshToken")
    public AuthenticationResponse refresh(@RequestBody @Valid RefreshTokenRq refreshTokenRq) {
        String refreshToken = refreshTokenRq.getRefresh_token();
        TokenObj tokenObj = (TokenObj) redisTemplate.opsForHash().get(refreshToken,refreshToken.hashCode());

        if (ObjectUtils.isEmpty(tokenObj)  ) {
            throw new RefreshTokenFailedException("Refresh Token isn't match or expired!");
        }
        String accessToken = keycloakService.getAccessToken(tokenObj.getEmail(), tokenObj.getPassword());
        return new AuthenticationResponse(accessToken, refreshTokenRq.getRefresh_token());


    }
}
