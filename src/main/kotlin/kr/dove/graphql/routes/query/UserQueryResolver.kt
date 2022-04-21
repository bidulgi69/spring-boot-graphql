package kr.dove.graphql.routes.query

import graphql.kickstart.tools.GraphQLQueryResolver
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kr.dove.graphql.persistence.User
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class UserQueryResolver(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : GraphQLQueryResolver {

     suspend fun users(count: Int, offset: Int): List<User> {
        return reactiveMongoTemplate
            .findAll(User::class.java)
            .skip(offset * count.toLong())
            .take(count.toLong())
            .asFlow()
            .toList()
    }

    fun user(id: String): Mono<User> {
        return reactiveMongoTemplate
            .find(Query.query(
                Criteria
                    .where("id").`is`(id)
            ), User::class.java)
            .elementAt(0)
            .onErrorReturn(
                User(
                    id = "Not Found.",
                    nickname = "Not Found."
                )
            )
    }
}