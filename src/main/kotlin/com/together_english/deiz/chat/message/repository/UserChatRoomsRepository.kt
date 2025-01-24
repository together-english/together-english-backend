package com.together_english.deiz.chat.message.repository

import com.together_english.deiz.chat.dto.ChatRoomListGetResponse
import com.together_english.deiz.chat.dto.UserChatRooms
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserChatRoomsRepository: MongoRepository<UserChatRooms, String> {
    fun findTop1ByRoomId(roomId: String): ChatRoomListGetResponse
    fun existsByRoomId(roomId: String): Boolean
    fun findByRoomId(roomId: String): Optional<UserChatRooms>
}