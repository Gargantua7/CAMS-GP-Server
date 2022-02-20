package com.gargantua7.cams.gp.server.config

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * @author Gargantua7
 */
class LocalDateTimeAdapter : JsonSerializer<LocalDateTime?>, JsonDeserializer<LocalDateTime?> {

    override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
    }

    override fun deserialize(element: JsonElement, type: Type?, context: JsonDeserializationContext?): LocalDateTime {
        val timestamp = element.asJsonPrimitive.asString
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
    }

}
