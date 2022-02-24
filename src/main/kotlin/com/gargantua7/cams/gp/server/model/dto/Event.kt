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
        this.id = this@Event.id
        this.name = this@Event.name
        this.content = this@Event.content
        this.number = this@Event.number
        this.location = this@Event.location
        this.eventTime = this@Event.eventTime
        this.startTime = this@Event.startTime
        this.endTime = this@Event.endTime
    }
}
