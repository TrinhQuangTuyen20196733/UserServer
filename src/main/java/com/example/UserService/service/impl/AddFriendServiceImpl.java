package com.example.UserService.service.impl;

import com.example.UserService.constant.FriendShipStatus;
import com.example.UserService.dto.request.RegisterReq;
import com.example.UserService.entity.FriendShip;
import com.example.UserService.entity.User;
import com.example.UserService.exception.BusinessLogicException;
import com.example.UserService.mapper.impl.UserMapper;
import com.example.UserService.repository.FriendShipRepository;
import com.example.UserService.repository.UserRepository;
import com.example.UserService.service.AddFriendService;
import com.example.UserService.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddFriendServiceImpl implements AddFriendService {
    private final FriendShipRepository friendShipRepository;
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public void addFriend(long userId) {
        String email = EmailUtils.getCurrentUser();
        if (ObjectUtils.isEmpty(email)) {
            throw new BusinessLogicException();
        }
        User sendUser = userRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException());
        User acceptUser = userRepository.findById(userId).orElseThrow(() -> new BusinessLogicException());
        FriendShip friendShip = FriendShip.builder()
                .sendFriend(sendUser)
                .acceptFriend(acceptUser)
                .status(FriendShipStatus.WAITING_ACCEPT)
                .build();

        friendShipRepository.save(friendShip);

    }

    @Override
    public void accept(long userId) {
        String email = EmailUtils.getCurrentUser();
        if (ObjectUtils.isEmpty(email)) {
            throw new BusinessLogicException();
        }
        FriendShip friendShip = friendShipRepository.findBySendFriendEmailAndAcceptUserIdAndStatus(email, userId, FriendShipStatus.WAITING_ACCEPT)
                .orElseThrow(() -> new BusinessLogicException());
        if (ObjectUtils.isEmpty(friendShip)) {
            throw new BusinessLogicException();
        }
        friendShip.setStatus(FriendShipStatus.FRIEND);
        friendShipRepository.save(friendShip);
    }

    @Override
    public void cancel(long userId) {
        String email = EmailUtils.getCurrentUser();
        if (ObjectUtils.isEmpty(email)) {
            throw new BusinessLogicException();
        }
        FriendShip friendShip = friendShipRepository.findCancelFriend(email, userId)
                .orElseThrow(() -> new BusinessLogicException());
        if (ObjectUtils.isEmpty(friendShip)) {
            throw new BusinessLogicException();
        }
        friendShip.setStatus(FriendShipStatus.NONE);
        friendShipRepository.save(friendShip);
    }

    @Override
    public List<RegisterReq> getPending(long userId) {
        String email = EmailUtils.getCurrentUser();
        if (ObjectUtils.isEmpty(email)) {
            throw new BusinessLogicException();
        }
        List<FriendShip> friendShipList = friendShipRepository.findPendingFriend(email)
                .orElseThrow(() -> new BusinessLogicException());
        if (ObjectUtils.isEmpty(friendShipList)) {
            throw new BusinessLogicException();
        }
        List<User> users =  friendShipList.stream()
                .map(friendShip -> friendShip.getAcceptFriend())

                .collect(Collectors.toList());
        return userMapper.toDTOList(users);
    }
}
