package kr.dove.graphql.routes.subscription

import com.fasterxml.jackson.databind.ObjectMapper
import graphql.kickstart.tools.GraphQLSubscriptionResolver
import kr.dove.graphql.exceptions.NotFoundException
import kr.dove.graphql.persistence.Message
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class MessageSubscriptionResolver(
    private val lettuceConnectionFactory: ReactiveRedisConnectionFactory,
    @Value("\${spring.data.redis.channel}") val channel: String,
    private val objectMapper: ObjectMapper,
) : GraphQLSubscriptionResolver, InitializingBean {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private var reactiveRedisMessageListenerContainer: ReactiveRedisMessageListenerContainer? = null

    fun receive(roomId: String): Publisher<Message> {
        logger.info("Listening request from socket... Room($roomId)")

        return reactiveRedisMessageListenerContainer ?. let { container ->
            container
                .receive(ChannelTopic("$channel:$roomId"))
                .flatMap {
                    Mono.just(
                        objectMapper.readValue(it.message, Message::class.java)
                    )
                }
        } ?: Mono.error(NotFoundException("Room($roomId) is not found..."))
    }

    override fun afterPropertiesSet() {
        reactiveRedisMessageListenerContainer = ReactiveRedisMessageListenerContainer(lettuceConnectionFactory)
    }
}