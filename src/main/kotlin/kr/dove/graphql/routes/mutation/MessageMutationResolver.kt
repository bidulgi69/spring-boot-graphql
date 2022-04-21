package kr.dove.graphql.routes.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import kr.dove.graphql.dto.message.CreateMessageInput
import kr.dove.graphql.persistence.Message
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class MessageMutationResolver(
    private val messageRedisTemplate: ReactiveRedisTemplate<String, Message>,
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    @Value("\${spring.data.redis.channel}") val channel: String,
): GraphQLMutationResolver {

    //  Logically, the number of channels in redis
    //  can exist as many as 2^32 per databases. (If the memory is infinite)
    //  https://stackoverflow.com/questions/59873784/redis-pub-sub-max-subscribers-and-publishers
    fun send(data: CreateMessageInput): Mono<Message> {
        val message = Message(
            UUID.randomUUID().toString(),
            data.room,
            data.sender,
            data.content
        )
        return messageRedisTemplate
            .convertAndSend(
                ChannelTopic("$channel:${data.room}").topic,
                message
            )
            .flatMap {
                //  If the message is successfully published to Redis,
                //  save the data to mongodb.
                reactiveMongoTemplate
                    .save(message)
            }
    }
}