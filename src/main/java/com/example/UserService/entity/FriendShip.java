package com.example.UserService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friend_ship")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendShip extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "send_friend_id")
    private User sendFriend;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accept_friend_id")
    private User acceptFriend;

    private int status;


}
