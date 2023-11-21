package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.entity.ChatMessage;
import com.chunjae.edumarket.entity.ChatRoom;
import com.chunjae.edumarket.per.ChatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private Map<String, ChatRoom> chatRooms;
    @Autowired
    private ChatMapper chatMapper;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }
    
    // 전체 채팅방 목록
    public List<ChatRoom> findAllRoom() {
        List<ChatRoom> chatRoomList = chatMapper.findAllRoom();
        for (ChatRoom cr: chatRoomList) {
            chatRooms.put(cr.getRoomId(), cr);
        }
        return new ArrayList<>(chatRooms.values());
    }

    // 특정 사용자의 전체 채팅방 목록 가져오기
    public List<ChatRoom> findAllRoom(String name) {
        List<ChatRoom> chatRoomList = chatMapper.findAllRoomWithName(name);
        for (ChatRoom cr : chatRoomList) {
            chatRooms.put(cr.getRoomId(), cr);
        }
        return new ArrayList<>(chatRooms.values());
    }
    
    // 채팅방 들어가기
    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }
    
    // 채팅방 만들기
    public int createRoom(ChatRoom chatRoom) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom2 = ChatRoom.builder()
                .roomId(randomId)
                .name(chatRoom.getName())
                .build();
        chatRooms.put(randomId, chatRoom2);
        chatRoom.setRoomId(randomId);
        int ck = chatMapper.createRoom(chatRoom);

        return ck;
    }

    // 채팅방의 채팅 내역 갖고오기
    public List<ChatMessage> findChatById(String roomId) {
        return chatMapper.findChatById(roomId);
    }

    // 채팅 내역 저장
    public int insertChat(ChatMessage chatMessage) {
        return chatMapper.insertChat(chatMessage);
    }

    // 중복된 채팅방이 있는지 검사
    public int findChatDist(ChatRoom chatRoom) {
        return chatMapper.findChatDist(chatRoom);
    }

    // 거래 완료된 채팅방 숨김처리
    public int actUpdate(int pno) {return chatMapper.actUpdate(pno);}
    
    // 채팅방 비활성화
    public int chatDsbld(String roomId) {return chatMapper.chatDsbld(roomId);}
}
