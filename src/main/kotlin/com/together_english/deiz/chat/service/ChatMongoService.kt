package com.together_english.deiz.chat.service

import com.together_english.deiz.chat.dto.ChatMessageDto
import com.together_english.deiz.chat.message.repository.ChatMessageRepository
import org.springframework.stereotype.Service

@Service
class ChatMongoService(
    private val chatMessageRepository: ChatMessageRepository
) {

    fun save(requestMessage: ChatMessageDto): ChatMessageDto {
        val message = requestMessage.toEntity(requestMessage)
        val savedMessage = chatMessageRepository.save(message)

        return savedMessage.toDTO(savedMessage)
    }
}