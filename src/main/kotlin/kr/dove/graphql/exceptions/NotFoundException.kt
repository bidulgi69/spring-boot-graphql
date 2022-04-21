package kr.dove.graphql.exceptions

class NotFoundException: Throwable {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}