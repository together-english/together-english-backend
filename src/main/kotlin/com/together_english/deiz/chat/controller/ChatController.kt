package com.together_english.deiz.chat.controller

import com.together_english.deiz.chat.dto.ChatMessageDto
import com.together_english.deiz.chat.service.ChatMongoService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "채팅 API", description = "채팅을 위한 API")
class ChatController(
    private val chatMongoService: ChatMongoService,
//    private val chatService: ChatService
) {
    /* WebSocketConfig prefix 설정 : "/pub" */
    @MessageMapping("/chat/message")
    fun message(
        message: ChatMessageDto,
        @Header("Authorization") accessToken: String,
    ) {
        val chatMessageDto = chatMongoService.save(message)
    }
}