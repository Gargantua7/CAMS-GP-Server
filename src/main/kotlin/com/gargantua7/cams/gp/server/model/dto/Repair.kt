package com.gargantua7.cams.gp.server.model.dto

import com.gargantua7.cams.gp.server.model.po.RepairEntity
import com.gargantua7.cams.gp.server.util.Snowflake
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
data class Repair(
    val id: Long = Snowflake.instance.nextId(),
    val title: String,
    val content: String,
    val initiator: String,
    val principal: String? = null,
    val initTime: LocalDateTime = LocalDateTime.now(),
    val updateTime: LocalDateTime = LocalDateTime.now(),
    val state: Boolean = true,
    val private: Boolean = false
) {
    constructor(that: RepairEntity) : this(
        that.id,
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
            id = this@Repair.id
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
