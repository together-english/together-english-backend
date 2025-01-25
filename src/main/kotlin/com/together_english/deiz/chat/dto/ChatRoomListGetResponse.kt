package com.together_english.deiz.chat.dto

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

data class ChatRoomListGetResponse(
    var roomId: String? = null,
    var senderId: Long? = null,
    var senderName: String? = null,
    var receiverId: Long? = null,
    var receiverName: String? = null,
    var lastMessage: String? = null,
    var time: Date? = Date.from(LocalDateTime.now().atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneOffset.ofHours(9)).toInstant()), // 전송 시간
) {
    fun getChatRoomNumber() {
        roomId = this.roomId
    }
}