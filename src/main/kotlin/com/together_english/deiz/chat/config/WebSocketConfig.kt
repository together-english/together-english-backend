package com.together_english.deiz.chat.config

import com.together_english.deiz.chat.StompHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.messaging.WebSocketStompClient

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val stompHandler: StompHandler
) : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/sub")
        config.setApplicationDestinationPrefixes("/pub")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        //TODO : setAllowedOrigins 설정 실제 프론트 연결 URL 로 변경필요
        registry.addEndpoint("/ws-stomp")
            .setAllowedOrigins("*")
            .withSockJS()
        registry.addEndpoint("/ws-stomp")
            .setAllowedOrigins("*")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        /* stompHandler token 체크를 위한 인터셉터 설정 */
        registration.interceptors(stompHandler)
    }

    /* 테스트코드에서 사용하기 위한 Bean 등록 */
    @Bean
    fun stompClient(): WebSocketStompClient {
        return WebSocketStompClient(StandardWebSocketClient())
    }
}