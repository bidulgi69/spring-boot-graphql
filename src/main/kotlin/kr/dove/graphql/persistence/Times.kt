package kr.dove.graphql.persistence

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import java.time.ZonedDateTime

abstract class Times {
    @CreatedBy
    var created: ZonedDateTime = ZonedDateTime.now()
        private set
    @LastModifiedBy
    var modified: ZonedDateTime = ZonedDateTime.now()
        private set
}