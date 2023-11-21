package com.chunjae.edumarket.entity;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatRoom {
    private String roomId;
    private String name;
    private String buyer;
    private String seller;
    private int pno;
    private String act;
    private String lastChat;
    private String lastDate;
    private Set<WebSocketSession> sessions = new HashSet<>();
    @Builder
    public ChatRoom(String roomId, String name, String buyer, String seller, int pno, String act) {
        this.roomId = roomId;
        this.name = name;
        this.buyer = buyer;
        this.seller= seller;
        this.pno = pno;
        this.act = act;
    }
}
