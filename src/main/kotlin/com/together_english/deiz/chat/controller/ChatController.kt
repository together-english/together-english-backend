package com.together_english.deiz.chat.controller

import com.together_english.deiz.chat.dto.ChatMessageDto
import com.together_english.deiz.chat.service.ChatMongoService
import com.together_english.deiz.chat.service.ChatService
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "채팅 API", description = "채팅을 위한 API")
class ChatController(
    private val chatMongoService: ChatMongoService,
    private val chatService: ChatService
) {
    private val logger = LoggerFactory.getLogger(ChatController::class.java)

    /* WebSocketConfig prefix 설정 : "/pub" */
    @MessageMapping("/chat/message")
    fun message(
        message: ChatMessageDto,
        @Header("Authorization") accessToken: String,
    ) {
        logger.info("MessageMapping Start ==========")
        // 채팅서버(MongoDB) 에 메시지 저장
        val chatMessageDto = chatMongoService.save(message)

        // Redis 서버에 채팅 발행(pub)
        chatService.sendChatMessage(chatMessageDto, accessToken)
    }
}