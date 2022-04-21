package kr.dove.graphql.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "messages")
data class Message(
    @Id val id: String,
    val room: String,
    val sender: String,
    val content: String,    //  not modifiable
): Times()