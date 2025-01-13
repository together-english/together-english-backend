package com.together_english.deiz.chat.config

import com.together_english.deiz.chat.StompHandler
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

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
        registry.addEndpoint("/ws-stomp")
            .setAllowedOrigins("*")
            //TODO : POSTMAN TEST 시에만 해당 설정 주석처리(이유: 아래 설정은 핸드셰이크 과정에서 SockJS-specific 요청이 필요)
            //.withSockJS()
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        /* stompHandler token 체크를 위한 인터셉터 설정 */
        registration.interceptors(stompHandler)
    }
}