package com.gargantua7.cams.gp.server.model.dto

import com.gargantua7.cams.gp.server.model.po.EventEntity
import com.gargantua7.cams.gp.server.util.Snowflake
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
data class Event(
    val id: Long = Snowflake.instance.nextId(),
    val name: String,
    val content: String,
    val number: Int,
    val location: String,
    val eventTime: LocalDateTime,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
) {
    constructor(that: EventEntity) : this(
        that.id,
        that.name,
        that.content,
        that.number,
        that.location,
        that.eventTime,
        that.startTime,
        that.endTime
    )

    val entity get() = EventEntity {
        id = this@Event.id
        name = this@Event.name
        content = this@Event.content
        number = this@Event.number
        location = this@Event.location
        eventTime = this@Event.eventTime
        startTime = this@Event.startTime
        endTime = this@Event.endTime
    }
}
