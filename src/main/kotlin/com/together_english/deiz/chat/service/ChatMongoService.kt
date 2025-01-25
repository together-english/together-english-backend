package com.together_english.deiz.chat.service

import com.together_english.deiz.chat.dto.ChatMessageDto
import com.together_english.deiz.chat.dto.UserChatRooms
import com.together_english.deiz.chat.dto.UserChatRoomsInfo
import com.together_english.deiz.chat.message.repository.ChatMessageRepository
import com.together_english.deiz.chat.message.repository.UserChatRoomsRepository
import org.springframework.stereotype.Service
import kotlin.NoSuchElementException

@Service
class ChatMongoService(
    private val userChatRoomsRepository: UserChatRoomsRepository,
    private val chatMessageRepository: ChatMessageRepository
) {
    fun save(requestMessage: ChatMessageDto): ChatMessageDto {
        val message = requestMessage.toEntity(requestMessage)

        val userChatRoomInfo = UserChatRooms(
            roomId = requestMessage.roomId,
            userChatRoomsInfo = UserChatRoomsInfo(
                senderId = requestMessage.senderId,
                senderName = requestMessage.senderName,
                receiverId = requestMessage.receiverId,
                receiverName = requestMessage.receiverName,
            ),
            lastMessage = requestMessage.message
        )

        val isChatRoomExist = userChatRoomsRepository.existsByRoomId(requestMessage.roomId)

        // 채팅방 리스트 정보 저장
        if(isChatRoomExist) {
            val existingRoom = userChatRoomsRepository.findByRoomId(requestMessage.roomId)
                                .orElseThrow { NoSuchElementException("chat room id : ${requestMessage.roomId} not found") }
            existingRoom.userChatRoomsInfo = userChatRoomInfo.userChatRoomsInfo
            existingRoom.lastMessage = userChatRoomInfo.lastMessage
            existingRoom.time = userChatRoomInfo.time

            userChatRoomsRepository.save(existingRoom)
        } else if(!isChatRoomExist){
            userChatRoomsRepository.save(userChatRoomInfo)
        }

        // 채팅 상세 정보 저장
        val savedMessage = chatMessageRepository.save(message)

        return savedMessage.toDTO(savedMessage)
    }
}