package com.together_english.deiz.chat

import com.fasterxml.jackson.databind.ObjectMapper
import com.together_english.deiz.chat.dto.MessageSubDto
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class RedisSubscriber(
    private val objectMapper: ObjectMapper,
    private val messagingTemplate: SimpMessagingTemplate,
) {
    private val logger = LoggerFactory.getLogger(RedisSubscriber::class.java)

    /*
    *   Redis 메시지 발행(pub)시
    *   대기하고있던 RedisSubscriber 에서 해당 메시지 처리
    */
    fun sendMessage(publishMessage: String) {
        try {
            val chatMessage = objectMapper.readValue(publishMessage, MessageSubDto::class.java).chatMessageDto

            logger.info("Redis Subscriber sendMessage - chatMSG: {}", chatMessage?.message)

            // 채팅방을 구독한 클라이언트에게 메시지 발송
            chatMessage?.let { messagingTemplate.convertAndSend("/sub/chat/room/${chatMessage?.roomId}", it) }

        } catch (e: Exception) {
            logger.error("sendMessage: Exception {}", e)
        }
    }

    fun sendRoomList(publishMessage: String) {
        try {
            val messageSubDto = objectMapper.readValue(publishMessage, MessageSubDto::class.java)
            val chatRoomListGetResponseList = messageSubDto.list
            val chatRoomListGetResponseListPartner = messageSubDto.partnerList

            val senderId = messageSubDto.senderId
            val receiverId = messageSubDto.receiverId

            // 로그인 유저 채팅방 리스트 최신화
            chatRoomListGetResponseList?.let {
                messagingTemplate.convertAndSend("/sub/chat/roomlist/$senderId", it)
                logger.info("Redis Subscriber sendRoomList : senderId={}", senderId)
            }

            // 상대방 유저 채팅방 리스트 최신화
            chatRoomListGetResponseListPartner?.let {
                messagingTemplate.convertAndSend("/sub/chat/roomlist/$receiverId",it)
                logger.info("Redis Subscriber sendRoomList : receiverId={}", receiverId)
            }
        } catch (e: Exception) {
            logger.error("sendRoomList: Exception {}", e)
        }
    }
}