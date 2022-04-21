package kr.dove.graphql.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id val id: String,
    var nickname: String,

): Times()