package com.chunjae.edumarket.per;

import com.chunjae.edumarket.entity.ChatMessage;
import com.chunjae.edumarket.entity.ChatRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {
    List<ChatRoom> findAllRoom(); // 채팅방 목록
    List<ChatRoom> findAllRoomWithName(String name);            // 채팅방 목록
    int createRoom(ChatRoom chatRoom); // 채팅방 생성
    int insertChat(ChatMessage chatMessage); // 채팅 내역 저장
    List<ChatMessage> findChatById(String roomId); // 채팅방의 채팅 내역 갖고오기
    int findChatDist(ChatRoom chatRoom); // 중복된 채팅방이 있는지 검사
    int actUpdate(int pno); // 거래 완료된 채팅방 숨김처리
    // 채팅방 비활성화
    int chatDsbld(String roomId); // 채팅방 비활성화 
}
