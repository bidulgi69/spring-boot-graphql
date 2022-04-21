package kr.dove.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SpringGraphqlApplication {

	@Bean
	fun objectMapper(): ObjectMapper {
		val mapper = ObjectMapper()
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
		mapper.registerModules(JavaTimeModule(), Jdk8Module())
		return mapper
	}
}

fun main(args: Array<String>) {
	runApplication<SpringGraphqlApplication>(*args)
}
