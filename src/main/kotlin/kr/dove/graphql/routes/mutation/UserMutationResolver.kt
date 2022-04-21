package kr.dove.graphql.routes.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import kr.dove.graphql.dto.user.CreateUserInput
import kr.dove.graphql.persistence.User
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserMutationResolver(
    private val template: ReactiveMongoTemplate,
) : GraphQLMutationResolver {

    fun createUser(data: CreateUserInput): Mono<User> {
        return template
            .save(User(
                data.id,
                data.nickname
            ))
    }

    fun deleteUser(id: String): Mono<String> {
        return template
            .findAndRemove(
                Query.query(
                    Criteria
                        .where("id").`is`(id)
                ), User::class.java
            )
            .flatMap { Mono.just(it.id) }
            .onErrorReturn("Not Found")
    }
}