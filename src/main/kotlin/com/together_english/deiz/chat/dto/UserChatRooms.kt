package com.together_english.deiz.chat.dto

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Document(collection = "UserChatRooms")
data class UserChatRooms(
    @Id
    val id: String? = null,

    @Field("roomId")
    @Indexed(unique = true)
    val roomId: String,                               // 방 번호
//    var roomName: String,                           // 방 이름
    var userChatRoomsInfo: UserChatRoomsInfo,         // 채팅방에 참가한 사용자 정보
    var lastMessage: String? = null,                  // 마지막 메시지 정보
    var time: Date? = Date.from(LocalDateTime.now().atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneOffset.ofHours(9)).toInstant()), // 전송 시간
)