package com.together_english.deiz.chat.dto

data class ChatRoomListGetResponse(
    var roomId: String? = null,
    var senderId: Long? = null,
    var senderName: String? = null,
    var receiverId: Long? = null,
    var receiverName: String? = null,
    var lastMessage: String? = null,
) {
    fun getChatRoomNumber() {
        roomId = this.roomId
    }
}