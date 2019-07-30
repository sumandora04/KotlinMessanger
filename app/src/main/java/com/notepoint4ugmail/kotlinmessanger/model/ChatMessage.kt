package com.notepoint4ugmail.kotlinmessanger.model

data class ChatMessage(
    val id:String = "",
    val text:String = "",
    val fromId:String="",
    val toId:String="",
    val timeStamp:Long =0L
) {
}