package kr.dove.graphql.config

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@ReadingConverter
class ZonedDateTimeReadConverter: Converter<Date, ZonedDateTime> {
    override fun convert(source: Date): ZonedDateTime {
        return source.toInstant().atZone(ZoneOffset.UTC)
    }
}

@WritingConverter
class ZonedDateTimeWriteConverter: Converter<ZonedDateTime, Date> {
    override fun convert(source: ZonedDateTime): Date {
        return Date.from(source.toInstant())
    }
}