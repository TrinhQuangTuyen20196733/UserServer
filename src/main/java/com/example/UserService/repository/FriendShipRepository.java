package com.example.UserService.repository;

import com.example.UserService.entity.FriendShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
    @Query("select fs from FriendShip  fs where fs.sendFriend.email  = :sendEmail and fs.acceptFriend.id = :acceptUserId and fs.status= :status")
    Optional<FriendShip> findBySendFriendEmailAndAcceptUserIdAndStatus(@Param("sendEmail") String email,
                                                                       @Param("acceptUserId") long acceptUserId, @Param("status") int status);

    @Query("select fs from FriendShip  fs where fs.sendFriend.email  = :sendEmail and fs.acceptFriend.id = :acceptUserId and fs.status in (100,200)")
    Optional<FriendShip> findCancelFriend(@Param("sendEmail") String email,
                                          @Param("acceptUserId") long acceptUserId);

    @Query("select fs from FriendShip  fs where fs.sendFriend.email  = :sendEmail  and fs.status =100")
    Optional<List<FriendShip>> findPendingFriend(@Param("sendEmail") String email);
}
