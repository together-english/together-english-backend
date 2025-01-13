package com.together_english.deiz.chat.dto

import com.amazonaws.services.kms.model.MessageType

data class ChatMessageDto(
    var id: String,
    var type: MessageType? = null,   // 메시지 타입 (ENTER, TALK, QUIT, DELETE.. 등 (임시))
    var roomId: String,      // 방 번호
    var userId: Long? = null,        // 사용자 ID
    var message: String? = null,     // 메시지
    var time: String? = null,        // 전송 시간
    var userCount: Long = 0L,        // 채팅방 인원 수
) {
    fun toEntity(chatMessageDto: ChatMessageDto): ChatMessage {
        return ChatMessage(
            id = this.id,
            type = this.type,
            roomId = this.roomId,
            userId = this.userId,
            message = this.message,
            time = this.time,
            userCount = this.userCount,
        )
    }
}