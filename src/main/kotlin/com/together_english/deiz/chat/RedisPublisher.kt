package com.together_english.deiz.chat

import com.together_english.deiz.chat.dto.MessageSubDto
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service

@Service
public class RedisPublisher(
    private val channelTopic: ChannelTopic,
    private val redisTemplate: RedisTemplate<String, Any>,
) {
    fun publish(message: MessageSubDto) {
        redisTemplate.convertAndSend(channelTopic.topic, message)
    }
}