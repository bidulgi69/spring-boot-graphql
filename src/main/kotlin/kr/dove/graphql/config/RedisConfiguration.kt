package kr.dove.graphql.config

import com.fasterxml.jackson.databind.ObjectMapper
import kr.dove.graphql.persistence.Message
import kr.dove.graphql.persistence.Room
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@ConfigurationProperties(prefix = "spring.data.redis")
@Configuration
class RedisConfiguration {
    lateinit var host: String
    lateinit var port: String

    //  Lettuce: Asynchronous
    //  Jedis: Synchronous
    @Primary
    @Bean
    fun lettuceConnectionFactory(): ReactiveRedisConnectionFactory {
        val config = LettuceClientConfiguration
            .builder()
            .commandTimeout(Duration.ofSeconds(1))
            .shutdownTimeout(Duration.ZERO)
            .build()
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host, port.toInt()), config)
    }

    @Bean
    fun roomRedisTemplate(lettuceConnectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, Room> {
        val serializationContext = RedisSerializationContext
            .newSerializationContext<String, Room>(StringRedisSerializer())
            //  hash operations
            //  format: <Main Key, Field, Value>
            .hashKey(StringRedisSerializer())
            .hashValue(Jackson2JsonRedisSerializer(Room::class.java))
            .build()
        return ReactiveRedisTemplate(lettuceConnectionFactory, serializationContext)
    }

    @Bean
    fun messageRedisTemplate(
        lettuceConnectionFactory: ReactiveRedisConnectionFactory,
        objectMapper: ObjectMapper
    ): ReactiveRedisTemplate<String, Message> {
        val serializationContext = RedisSerializationContext
            .newSerializationContext<String, Message>(StringRedisSerializer())
            .key(StringRedisSerializer())
            .value(Jackson2JsonRedisSerializer(Message::class.java). apply {
                this.setObjectMapper(objectMapper)
            })
            .build()
        return ReactiveRedisTemplate(lettuceConnectionFactory, serializationContext)
    }
}