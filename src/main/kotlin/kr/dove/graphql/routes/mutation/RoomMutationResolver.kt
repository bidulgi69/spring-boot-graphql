package kr.dove.graphql.routes.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import kr.dove.graphql.dto.room.ConnectRoomInput
import kr.dove.graphql.dto.room.CreateRoomInput
import kr.dove.graphql.dto.room.DisconnectRoomInput
import kr.dove.graphql.exceptions.NotFoundException
import kr.dove.graphql.persistence.Room
import kr.dove.graphql.persistence.User
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class RoomMutationResolver(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : GraphQLMutationResolver {

    fun createRoom(data: CreateRoomInput): Mono<Room> {
        return reactiveMongoTemplate
            .save(Room(
                id = UUID.randomUUID().toString(),
                data.title,
                mutableSetOf()
            ))
    }

    fun deleteRoom(id: String): Mono<String> {
        return reactiveMongoTemplate
            .findAndRemove(
                Query.query(
                    Criteria
                        .where("id").`is`(id)
                ), Room::class.java
            )
            .flatMap { Mono.just(it.title) }
    }

    fun connect(data: ConnectRoomInput): Mono<Room> {
        val user: Mono<User> = reactiveMongoTemplate.find(
            Query.query(
                Criteria
                    .where("id").`is`(data.userId)
            ), User::class.java
        )
            .switchIfEmpty(Mono.error(NotFoundException("Not Found")))
            .elementAt(0)

        val room: Mono<Room> = reactiveMongoTemplate.find(
            Query.query(
                Criteria
                    .where("id").`is`(data.roomId)
            ), Room::class.java
        )
            .switchIfEmpty(Mono.error(NotFoundException("Not Found")))
            .elementAt(0)

        return Mono.zip(
            user,
            room
        ).flatMap {
            reactiveMongoTemplate.save(
                it.t2.apply {
                    this.users.add(it.t1)
                }
            )
        }
    }

    fun disconnect(data: DisconnectRoomInput): Mono<Room> {
        val room: Mono<Room> = reactiveMongoTemplate.find(
            Query.query(
                Criteria
                    .where("id").`is`(data.roomId)
            ), Room::class.java
        )
            .switchIfEmpty(Mono.error(NotFoundException("Not Found")))
            .elementAt(0)

        return room
            .flatMap {
                reactiveMongoTemplate.save(
                    it.apply {
                        this.users.removeIf {v ->
                            v.id == data.userId
                        }
                    }
                )
            }
    }
}