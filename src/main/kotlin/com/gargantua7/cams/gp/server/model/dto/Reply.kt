package com.gargantua7.cams.gp.server.model.dto

import com.gargantua7.cams.gp.server.model.po.ReplyEntity
import java.time.LocalDateTime
import java.util.*

/**
 * @author Gargantua7
 */
data class Reply(
    val uuid: String = UUID.randomUUID().toString().replace("-", ""),
    val repair: String,
    val sender: String,
    val type: Int = 0,
    val content: String,
    val time: LocalDateTime = LocalDateTime.now()
) {
    constructor(that: ReplyEntity) : this(
        that.uuid,
        that.repair,
        that.sender,
        that.type,
        that.content,
        that.time
    )

    val entity
        get() = ReplyEntity {
            this.uuid = this@Reply.uuid
            this.repair = this@Reply.repair
            this.sender = this@Reply.sender
            this.type = this@Reply.type
            this.content = this@Reply.content
            this.time = this@Reply.time
        }
}
