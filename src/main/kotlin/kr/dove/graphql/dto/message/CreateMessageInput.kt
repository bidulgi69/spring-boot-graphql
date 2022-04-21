package kr.dove.graphql.dto.message

data class CreateMessageInput(
    val sender: String,
    val room: String,
    val content: String,
)