package com.together_english.deiz.chat.service

import com.together_english.deiz.chat.RedisPublisher
import com.together_english.deiz.chat.dto.ChatMessageDto
import com.together_english.deiz.chat.dto.ChatRoomListGetResponse
import com.together_english.deiz.chat.dto.MessageSubDto
import com.together_english.deiz.chat.repository.ChatRoomRedisRepository
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val redisPublisher: RedisPublisher,
    private val chatRoomRedisRepository: ChatRoomRedisRepository,
    private val chatRoomService: ChatRoomService
) {

    /* 채팅방 내 메시지 발행(Pub) */
    fun sendChatMessage(chatMessage: ChatMessageDto, accessToken: String) {
        //TODO 0.토큰을 통해 사용자 검증

        //TODO : 마지막 메시지를 레디스에 해시 자료구조에 저장이 필요할까? -> Pub/Sub 통신으로 실시간 처리가 될건데..
        chatRoomRedisRepository.setLastChatMessage(chatMessage.roomId, chatMessage)

        val chatRoomListGetResponse = chatMessage.toChatRoomListGetResponse(chatMessage)
        val chatUserIds = listOf(chatMessage.senderId!!, chatMessage.receiverId!!)

        // 발신,수신자 채팅방 목록 정보 최신화
        for (userId in chatUserIds) {
            chatRoomRedisRepository.setChatRoomList(userId, chatMessage.roomId)
            chatRoomRedisRepository.setChatRoomListDetail(userId, chatMessage.roomId, chatRoomListGetResponse)
        }

        var senderId = chatMessage.senderId!!
        var partnerId: Long

        /* 채팅 상세정보 저장 */
        chatRoomRedisRepository.setChatRoomDetail(chatMessage)


        // TODO 테스트용 채팅방 목록 조회 - 추후에 조회 메소드에 옮기기
        val chatRoomListWithDetail: List<ChatRoomListGetResponse> = getRedisChatRoomListWithDetail(senderId)
        println(chatRoomListWithDetail.toString())

        // TODO 테스트용 채팅방 상세메시지들 조회 - 추후에 조회 메소드로 옮기기
        val chatRoomDetail = getRedisChatRoomDetail("1")
        print(chatRoomDetail)

        //TODO 1.채팅방 삭제 시 DELETE 로직

        /* 채팅방의 마지막 메시지 조회 */
        val newChatRoom: ChatRoomListGetResponse
        if (chatRoomRedisRepository.existChatRoom(senderId, chatMessage.roomId)) {
            newChatRoom = chatRoomRedisRepository.getChatRoom(chatMessage.roomId)
        } else {
            newChatRoom =
                chatRoomService.getChatRoomInfo(accessToken, chatMessage.roomId)
        }

        val message = MessageSubDto(
            senderId = senderId,
            receiverId = chatMessage.receiverId,
            chatMessageDto = ChatMessageDto(
                type = chatMessage.type,
                roomId = chatMessage.roomId,
                senderId = chatMessage.senderId,
                senderName = chatMessage.senderName,
                receiverId = chatMessage.receiverId,
                receiverName = chatMessage.receiverName,
                message = chatMessage.message,
                time = chatMessage.time,
                userCount = chatMessage.userCount,
            ),
        )

        redisPublisher.publish(message)
    }

    /* 사용자의 채팅 목록, 마지막 메시지를 조회 */
    fun getRedisChatRoomListWithDetail(senderId: Long): List<ChatRoomListGetResponse> {
        // 테스트용 - Redis 저장소에서 최신화된 채팅방 목록 정보 조회
        val roomIds = chatRoomRedisRepository.getChatRoomList(senderId)

        // Redis 저장소에 저장된 채팅방 마지막 메시지 정보 조회
        return chatRoomRedisRepository.getChatRoomListDetail(senderId, roomIds!!)
    }

    /* 사용자의 채팅상세정보 조회 */
    fun getRedisChatRoomDetail(roomId: String): List<ChatMessageDto> {
        val chatMessage = chatRoomRedisRepository.getChatRoomDetail(roomId)
        return chatMessage
    }
}