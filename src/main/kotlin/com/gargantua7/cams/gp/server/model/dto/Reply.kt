package com.gargantua7.cams.gp.server.model.dto

import com.gargantua7.cams.gp.server.model.po.ReplyEntity
import com.gargantua7.cams.gp.server.util.Snowflake
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
data class Reply(
    val uuid: Long = Snowflake.instance.nextId(),
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
            this.id = this@Reply.uuid
            this.repair = this@Reply.repair
            this.sender = this@Reply.sender
            this.type = this@Reply.type
            this.content = this@Reply.content
            this.time = this@Reply.time
        }
}
