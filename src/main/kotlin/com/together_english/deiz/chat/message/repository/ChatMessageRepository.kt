package com.together_english.deiz.chat.message.repository

import com.together_english.deiz.chat.dto.ChatMessage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

interface ChatMessageRepository: MongoRepository<ChatMessage, String> {
}