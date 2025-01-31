package com.together_english.deiz.chat.dto

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ChatRoomListGetResponse(
    var roomId: String? = null,
    var senderId: Long? = null,
    var senderName: String? = null,
    var receiverId: Long? = null,
    var receiverName: String? = null,
    var lastMessage: String? = null,
    var time: String = LocalDateTime.now()             // 현재 시스템 시간
        .format(DateTimeFormatter.ISO_DATE_TIME),       // ISO-8601 형식으로 변환, // 전송 시간,
) {
    fun getChatRoomNumber() {
        roomId = this.roomId
    }
}