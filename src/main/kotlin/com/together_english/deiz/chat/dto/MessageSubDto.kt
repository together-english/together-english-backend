package com.together_english.deiz.chat.dto

data class MessageSubDto(
    var senderId: Long? = null,
    var receiverId: Long? = null,
    var chatMessageDto: ChatMessageDto? = null,
    var list: List<ChatRoomListGetResponse>? = null,
    var partnerList: List<ChatRoomListGetResponse>? = null
) {

}