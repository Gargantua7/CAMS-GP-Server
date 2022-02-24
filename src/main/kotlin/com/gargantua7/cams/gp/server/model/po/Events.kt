package com.gargantua7.cams.gp.server.model.po

import com.gargantua7.cams.gp.server.model.dto.Event
import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
object Events : Table<EventEntity>("event") {

    val id = long("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val content = text("content").bindTo { it.content }
    val number = int("number").bindTo { it.number }
    val location = text("location").bindTo { it.location }
    val eventTime = datetime("event_time").bindTo { it.eventTime }
    val startTime = datetime("start_time").bindTo { it.startTime }
    val endTime = datetime("end_time").bindTo { it.endTime }
}

interface EventEntity : Entity<EventEntity> {

    companion object : Entity.Factory<EventEntity>()

    var id: Long
    var name: String
    var content: String
    var number: Int
    var location: String
    var eventTime: LocalDateTime
    var startTime: LocalDateTime
    var endTime: LocalDateTime

    val value get() = Event(this)
}
