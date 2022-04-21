package kr.dove.graphql.exceptions.handler

import kr.dove.graphql.exceptions.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebExchange

@RestControllerAdvice
class ExceptionHandlers {

    @ExceptionHandler(NotFoundException::class)
    @ResponseBody fun handleNotFoundException(exception: NotFoundException, exchange: ServerWebExchange): HttpError {
        exchange.response.statusCode = HttpStatus.NOT_FOUND
        return HttpError(
            HttpStatus.NOT_FOUND,
            exception.message ?: exception.localizedMessage
        )
    }
}

data class HttpError (
    val status: HttpStatus,
    val message: String,
)