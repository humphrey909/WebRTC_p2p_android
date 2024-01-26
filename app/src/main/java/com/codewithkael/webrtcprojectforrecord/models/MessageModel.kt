package com.codewithkael.webrtcprojectforrecord.models

data class MessageModel(
     val userid: String? = null, // 유저 아이디
     val room: String? = null, // 방
     val data:Any?=null // 보낼 데이터
)
