package com.together_english.deiz.chat.config

import com.together_english.deiz.chat.RedisSubscriber
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
public class RedisConfig(
    /* Redis yml 파일 설정 */
    private val redisProperties: RedisProperties,
) {
    /* 단일 Topic 을 위한 설정 */
    @Bean
    fun channelTopic(): ChannelTopic {
        return ChannelTopic("chatroom")
    }

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val lettuceFactory = LettuceConnectionFactory()
        lettuceFactory.hostName = redisProperties.host
        lettuceFactory.port = redisProperties.port
        return lettuceFactory
    }

    @Bean
    fun listenerAdapterChatMessage(
        subscriber: RedisSubscriber
    ): MessageListenerAdapter {
        return MessageListenerAdapter(subscriber, "sendMessage")
    }

    /* redis 에 발행(pub)된 메시지 처리 시 리스너 */
    @Bean
    fun redisMessageListener(
        listenerAdapterChatMessage: MessageListenerAdapter,
        channelTopic: ChannelTopic
    ): RedisMessageListenerContainer {

        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(redisConnectionFactory())
        container.addMessageListener(listenerAdapterChatMessage, channelTopic)
        return container
    }

    /** 실제 메시지 방을 처리하는 subscriber 설정 추가 */
    @Bean
    fun listenerAdapterChatRoomList(subscriber: RedisSubscriber): MessageListenerAdapter {
        return MessageListenerAdapter(subscriber, "sendRoomList")
    }

    @Bean
    fun redisMessageListenerRoomList(
        listenerAdapterChatRoomList: MessageListenerAdapter,
        channelTopic: ChannelTopic
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(redisConnectionFactory())
        container.addMessageListener(listenerAdapterChatRoomList, channelTopic)
        return container
    }

    /* RedisPublisher 에서 사용되는 redisTemplate 설정 */
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory)
        : RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.connectionFactory = connectionFactory
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(Any::class.java)

        redisTemplate.hashKeySerializer = StringRedisSerializer()
        redisTemplate.hashValueSerializer = Jackson2JsonRedisSerializer(Any::class.java)

        return redisTemplate
    }
}