package kr.dove.graphql.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "rooms")
data class Room(
    @Id val id: String,
    var title: String,
    var users: MutableSet<User>
): Times()
