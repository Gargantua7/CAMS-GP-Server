package com.gargantua7.cams.gp.server.model.vo

import com.gargantua7.cams.gp.server.exception.BadRequestException
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
data class NewEventModel(
    val name: String,
    val content: String,
    val number: Int,
    val location: String,
    val eventTime: LocalDateTime,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
) {
    init {
        require(name.length !in 1..20) { "Name Too Long Or Too Short" }
        require(number < 0) { "Number of people cannot be negative" }
        require(endTime < startTime) { "End time cannot be earlier than start time" }
        require(eventTime < startTime) { "Event time cannot be earlier than start time" }
    }
}
