package kr.dove.graphql.routes.query

import graphql.kickstart.tools.GraphQLQueryResolver
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kr.dove.graphql.persistence.Room
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class RoomQueryResolver(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : GraphQLQueryResolver {

    suspend fun rooms(): List<Room> {
        return reactiveMongoTemplate
            .findAll(Room::class.java)
            .asFlow()
            .toList()
    }

    fun room(id: String): Mono<Room> {
        return reactiveMongoTemplate
            .find(Query.query(
                Criteria
                    .where("id").`is`(id)
            ), Room::class.java)
            .elementAt(0)
            .onErrorReturn(Room(
                id = "Not Found.",
                title = "Not Found.",
                users = mutableSetOf()
            ))
    }
}