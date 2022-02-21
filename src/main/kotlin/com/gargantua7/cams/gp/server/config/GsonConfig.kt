package com.gargantua7.cams.gp.server.config

import com.google.gson.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.GsonHttpMessageConverter
import org.springframework.lang.Nullable
import java.io.Writer
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * @author Gargantua7
 */
@Configuration
class GsonConfig {

    @Bean
    fun gson(): Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
        .create()

    @Bean
    fun extendMessageConverters(gson: Gson): GsonHttpMessageConverter {
        return object : GsonHttpMessageConverter() {
            override fun writeInternal(any: Any, @Nullable type: Type?, writer: Writer) {
                if (type is ParameterizedType && any.javaClass.isAssignableFrom(type.javaClass)) {
                    gson.toJson(any, type, writer)
                } else {
                    gson.toJson(any, writer)
                }
            }
        }.apply {
            setGson(gson)
        }
    }

    object LocalDateTimeAdapter : JsonSerializer<LocalDateTime?>, JsonDeserializer<LocalDateTime?> {

        override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(src?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
        }

        override fun deserialize(element: JsonElement, type: Type?, context: JsonDeserializationContext?): LocalDateTime {
            val timestamp = element.asJsonPrimitive.asString
            return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
        }

    }
}

