package com.together_english.deiz.chat

import com.mongodb.client.MongoClient
import com.together_english.deiz.chat.config.MongoConfig
import com.together_english.deiz.chat.config.WebSocketConfig
import com.together_english.deiz.chat.controller.ChatController
import com.together_english.deiz.chat.message.repository.ChatMessageRepository
import com.together_english.deiz.chat.service.ChatMongoService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.messaging.simp.stomp.*
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.lang.reflect.Type
import java.net.URI
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
    properties = [
        "jwt.secret=dummy-secret",
        "cloud.aws.credentials.access-key=dummy-access-key",
        "cloud.aws.credentials.secret-key=dummy-secret-key",
        "cloud.aws.region.static=dummy-region",
        "jwt.accessExpiration=241233",
        "jwt.refreshExpiration=342432",
        "cloud.aws.s3.bucket=dummy",
        "auth.mail-api.key=your-api-key-value",
        "auth.mail-api.secret-key=your-api-secret-key-value",
        "domain.url=dummy-domain",
    ]
)

class ChatControllerTest {

    @Autowired
    private lateinit var stompClient: WebSocketStompClient

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var chatController: ChatController
    @MockBean
    private lateinit var chatMongoService: ChatMongoService
    @MockBean
    private lateinit var chatMessageRepository: ChatMessageRepository
    @MockBean
    private lateinit var mongoClient: MongoClient
    @MockBean
    private lateinit var mongoTemplate: MongoTemplate
    @MockBean
    private lateinit var gridFsTemplate: GridFsTemplate

    @BeforeEach
    fun setUp() {
        // stompClient가 테스트 메소드 실행 전에 초기화되도록 설정
        println("stompClient initialized: $stompClient")
    }

    @Test
    fun testWebSocketAndStompConnect() {
        //Websocket Connect
        val latch = CountDownLatch(1) // 연결을 기다리기 위한 latch
        val sessionHandler = object : StompSessionHandlerAdapter() {
            override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
                println("Connected to WebSocket")
                session.subscribe("/sub/chat/message", object : StompFrameHandler {
                    override fun getPayloadType(headers: StompHeaders): Type = String::class.java
                    override fun handleFrame(headers: StompHeaders, payload: Any?) {
                        println("Received message: $payload")
                    }
                })
                session.send("/pub/chat/message", "Chat Message Test!!")
            }
        }

        val url = URI("ws://localhost:80/ws-stomp")
        val session = stompClient.connectAsync(url.toString(), sessionHandler)
        latch.await(5, TimeUnit.SECONDS)
    }
}