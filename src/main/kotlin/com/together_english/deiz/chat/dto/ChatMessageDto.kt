package com.together_english.deiz.chat.dto

import com.together_english.deiz.chat.MessageType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ChatMessageDto(
    val id: String? = null,
    val roomId: String = "dummy_roomId", // 방 번호
    var type: MessageType? = null,   // 메시지 타입 (ENTER, TALK, QUIT, DELETE.. 등 (임시))
    var senderId: Long? = null,      // 전송자 ID
    var senderName: String? = null,  // 전송자명
    var receiverId: Long? = null,    // 수신자 ID
    var receiverName: String? = null,// 수신자명
    var message: String? = null,     // 메시지
    var time: String = LocalDateTime.now()             // 현재 시스템 시간
        .format(DateTimeFormatter.ISO_DATE_TIME),       // ISO-8601 형식으로 변환, // 전송 시간,
    var userCount: Long = 0L,        // 채팅방 인원 수,
) {
    fun toEntity(chatMessageDto: ChatMessageDto): ChatMessage {
        return ChatMessage(
            type = this.type,
            roomId = this.roomId,
            senderId = this.senderId,
            senderName = this.senderName,
            receiverId = this.receiverId,
            receiverName = this.receiverName,
            message = this.message,
            userCount = this.userCount,
        )
    }

    fun toChatRoomListGetResponse(chatMessage: ChatMessageDto): ChatRoomListGetResponse {
        return ChatRoomListGetResponse(
            roomId = chatMessage.roomId,
            senderId = chatMessage.senderId,
            senderName = chatMessage.senderName,
            receiverId = chatMessage.receiverId,
            receiverName = chatMessage.receiverName,
            lastMessage = chatMessage.message,
        )
    }
}