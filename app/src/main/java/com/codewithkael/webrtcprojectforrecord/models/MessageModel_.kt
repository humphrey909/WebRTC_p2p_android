package com.codewithkael.webrtcprojectforrecord.models

data class MessageModel_(
     val type: String,
     val name: String? = null,
     val target: String? = null,
     val data:Any?=null
)
