package com.gargantua7.cams.gp.server.model.dto

import com.gargantua7.cams.gp.server.model.po.ReplyEntity
import com.gargantua7.cams.gp.server.util.Snowflake
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
data class Reply(
    val id: Long = Snowflake.instance.nextId(),
    val repair: Long,
    val sender: String,
    val type: Int = 0,
    val content: String,
    val time: LocalDateTime = LocalDateTime.now()
) {
    constructor(that: ReplyEntity) : this(
        that.id,
        that.repair,
        that.sender,
        that.type,
        that.content,
        that.time
    )

    val entity
        get() = ReplyEntity {
            id = this@Reply.id
            repair = this@Reply.repair
            sender = this@Reply.sender
            type = this@Reply.type
            content = this@Reply.content
            time = this@Reply.time
        }
}
