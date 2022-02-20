package com.gargantua7.cams.gp.server.model.dto

import com.gargantua7.cams.gp.server.config.LocalDateTimeAdapter
import com.gargantua7.cams.gp.server.model.po.RepairEntity
import com.google.gson.annotations.JsonAdapter
import java.time.LocalDateTime
import java.util.*

/**
 * @author Gargantua7
 */
data class Repair(
    val uuid: String = UUID.randomUUID().toString().replace("-", ""),
    val title: String,
    val content: String,
    val initiator: String,
    val principal: String? = null,
    @JsonAdapter(LocalDateTimeAdapter::class)
    val initTime: LocalDateTime = LocalDateTime.now(),
    @JsonAdapter(LocalDateTimeAdapter::class)
    val updateTime: LocalDateTime = LocalDateTime.now(),
    val state: Boolean = true,
    val private: Boolean = false
) {
    constructor(that: RepairEntity) : this(
        that.uuid,
        that.title,
        that.content,
        that.initiator,
        that.principal,
        that.initTime,
        that.updateTime,
        that.state,
        that.private
    )

    val entity
        get() = RepairEntity {
            uuid = this@Repair.uuid
            title = this@Repair.title
            content = this@Repair.content
            initiator = this@Repair.initiator
            principal = this@Repair.principal
            initTime = this@Repair.initTime
            updateTime = this@Repair.updateTime
            state = this@Repair.state
            private = this@Repair.private
        }
}
