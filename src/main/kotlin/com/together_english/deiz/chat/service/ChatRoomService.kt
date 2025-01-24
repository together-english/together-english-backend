package com.together_english.deiz.chat.service

import com.together_english.deiz.chat.dto.ChatRoomListGetResponse
import com.together_english.deiz.chat.message.repository.UserChatRoomsRepository
import com.together_english.deiz.chat.repository.ChatRoomRedisRepository
import com.together_english.deiz.security.util.JwtUtil
import org.springframework.stereotype.Service

/*
* 채팅방 정보와 기록을 관리하는 서비스 레이어
* 채팅방 정보 : MongoDB 서버에 저장
*
*/
@Service
class ChatRoomService(
    private val chatRoomRedisRepository: ChatRoomRedisRepository,
    private val userChatRoomsRepository: UserChatRoomsRepository,
    private val jwtUtil: JwtUtil
) {

    /*
    * 채팅방 정보 조회
    * MongoDB 서버 조회
    */
    fun getChatRoomInfo(jwtToken: String, roomId: String)
            : ChatRoomListGetResponse {
        val token = jwtToken.substring(7)
        val validate = jwtUtil.validateToken(token)
        require(validate) { "로그인 검증에 실패하였습니다. 재 로그인 후 시도해주세요." }

        return userChatRoomsRepository.findTop1ByRoomId(roomId)
    }
}