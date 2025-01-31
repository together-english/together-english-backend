package com.together_english.deiz.chat.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.together_english.deiz.chat.dto.ChatMessageDto
import com.together_english.deiz.chat.dto.ChatRoomListGetResponse
import jakarta.annotation.Resource
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.ListOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SetOperations
import org.springframework.stereotype.Repository
import java.lang.Boolean.TRUE

/*
*   채팅방 정보들을 Redis 서버와 통신(Read, Write)하는 역할
*   저장시에는 Redis 해시 자료구조 사용
*/
@Repository
class ChatRoomRedisRepository(
    private val redisTemplate: RedisTemplate<String, Object>,
    private val redisSetTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(ChatRoomRedisRepository::class.java)


    /* Redis - redisTemplate Bean 의존성 주입 */
    @Resource(name = "redisTemplate")
    private lateinit var opsHashChatRoomList: SetOperations<String, String>
    private lateinit var opsHashChatRoomListInfo: HashOperations<String, String, ChatRoomListGetResponse>
    private lateinit var opsHashChatRoom: HashOperations<String, String, ChatMessageDto>
    private lateinit var opsListChatRoomDetail: ListOperations<String, Object>

    // 채팅방 리스트 초기화
//    fun initChatRoomList(userId: Long, list: List<ChatRoomListGetResponse>) {
//        if (redisTemplate.hasKey(getChatRoomListKey(userId))) {
//            redisTemplate.delete(getChatRoomListKey(userId))
//        }
//
//        opsHashChatRoomList = redisTemplate.opsForHash()
//        for (chatRoomListGetResponse in list) {
//            //TODO - Redis 에 채팅방 저장
//            opsHashChatRoomList.put(
//                userId.toString(),
//                chatRoomListGetResponse.getChatRoomNumber().toString(),
//                chatRoomListGetResponse
//            )
//        }
//    }

//    fun getChatRoomList(userId: Long): List<ChatRoomListGetResponse> {
//        return objectMapper.convertValue(
//            opsHashChatRoomList.values(getChatRoomListKey(userId)),
//            object : TypeReference<List<ChatRoomListGetResponse>>() {}
//        )
//    }

    // TODO : getChatRoom 메소드 사용 로직위치 확인
    fun getChatRoom(roomId: String): ChatRoomListGetResponse {
        return objectMapper.convertValue(
            opsHashChatRoom.values(getChatRoomKey(roomId)),
            object : TypeReference<ChatRoomListGetResponse>() {}
        )
    }

    /* 채팅 상세정보 저장 */
    fun setChatRoomDetail(chatMessage: ChatMessageDto) {
        val chatRoomDetailKey = getChatRoomDetailKey(chatMessage.roomId!!)
        opsListChatRoomDetail = redisTemplate.opsForList()
        opsListChatRoomDetail.rightPush(chatRoomDetailKey, chatMessage as Object)
    }

    /* 채팅 상세정보 조회 */
    fun getChatRoomDetail(roomId: String): List<ChatMessageDto> {
        val chatRoomDetailKey = getChatRoomDetailKey(roomId)
        opsListChatRoomDetail = redisTemplate.opsForList()
        val redisChatRoomDetail = opsListChatRoomDetail.range(chatRoomDetailKey, 0, -1)
        val chatRoomDetail = redisChatRoomDetail!!.map {
            objectMapper.convertValue(it, ChatMessageDto::class.java)
        }

        // 채팅 메시지 오름차순 정렬
        val sortedChatRoomDetail = chatRoomDetail.sortedBy { it.time }
        return sortedChatRoomDetail
    }

    /*
        Redis 에 채팅방 RoomId 의 마지막 메시지를 설정
     */
    fun setLastChatMessage(roomId: String, chatMessage: ChatMessageDto) {
        // Redis 해시 자료구조 사용
        val lastMessageKey = "chat_room:last_message$roomId"
        opsHashChatRoom = redisTemplate.opsForHash()
        opsHashChatRoom.put(lastMessageKey, roomId, chatMessage)
        logger.info("setLastChatMessage success $lastMessageKey")
    }

    /*
     * Redis - Set 자료구조 형태로 채팅방 목록 저장
     */
    fun setChatRoomList(userId: Long, roomId: String) {
        val chatRoomListKey = getChatRoomListKey(userId)
        opsHashChatRoomList = redisSetTemplate.opsForSet()
        opsHashChatRoomList.add(chatRoomListKey, roomId)
    }

    /*
     * Redis - Set 자료구조 형태로 채팅방 목록 조회
     */
    fun getChatRoomList(userId: Long): MutableSet<String>? {
        val chatRoomListKey = getChatRoomListKey(userId)
        opsHashChatRoomList = redisSetTemplate.opsForSet()
        return opsHashChatRoomList.members(chatRoomListKey)
    }

    fun setChatRoomListDetail(userId: Long, roomId: String, request: ChatRoomListGetResponse) {
        val chatRoomListKey = getChatRoomListDetailKey(userId, roomId)
        opsHashChatRoomListInfo = redisTemplate.opsForHash()
        opsHashChatRoomListInfo.put(chatRoomListKey, roomId, request)
    }

    /* 사용자의 채팅 목록, 마지막 메시지들을 조회 */
    fun getChatRoomListDetail(userId: Long, roomIds: MutableSet<String>): List<ChatRoomListGetResponse> {
        val chatRoomListWithDetail = ArrayList<ChatRoomListGetResponse>()

        roomIds.forEach { roomId ->
            val chatRoomListDetailKey = getChatRoomListDetailKey(userId, roomId)
            opsHashChatRoomListInfo = redisTemplate.opsForHash()
            val chatRoomListInfo = opsHashChatRoomListInfo.values(chatRoomListDetailKey)
            chatRoomListWithDetail.addAll(chatRoomListInfo)
        }

        // 내림차순으로 정렬하여 채팅방 리스트 반환
        val sortedChatRoomList = chatRoomListWithDetail.sortedByDescending { it.time }
        return sortedChatRoomList
    }

    // Redis - 채팅방 리스트에 대한 Key
    // user:{userId}:rooms 형태로 저장
    fun getChatRoomListKey(userId: Long): String {
        return "user:$userId:rooms"
    }

    fun getChatRoomListDetailKey(userId: Long, roomId: String): String {
        return "user:$userId:room:$roomId"
    }

    fun getChatRoomKey(roomId: String): String {
        return "test:dummy"
    }

    // Redis - 채팅방 단건 상세정보 저장 Key
    // room:{roomId} 형태로 저장
    fun getChatRoomDetailKey(roomId: String): String {
        return "room:$roomId"
    }

    /*
    * 사용자의 채팅방 정보가 Redis 서버에 존재하는지 유무체크
    */
    fun existChatRoom(userId: Long, roomId: String): Boolean {
        var key = generateRedisKey(userId, roomId)
        return TRUE.equals(redisTemplate.hasKey(key))
    }

    /*
    *  Redis Key 생성 - EX) chatroom:[userId]:[roomId]
    *  각각의 채팅방에 대한 데이터 관리 시 사용
    */
    fun generateRedisKey(userId: Long, roomId: String): String {
        return "chatroom:$userId:$roomId"
    }
}