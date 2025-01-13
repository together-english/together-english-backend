package com.together_english.deiz.chat

import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

/*
    WebSocket 요청이 처리되기 전 핸들러
 */
@Component
public class StompHandler : ChannelInterceptor {
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        return super.preSend(message, channel)
    }
}