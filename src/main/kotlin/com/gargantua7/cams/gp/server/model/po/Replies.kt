package com.gargantua7.cams.gp.server.model.po

import com.gargantua7.cams.gp.server.model.dto.Reply
import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
object Replies : Table<ReplyEntity>("repair_reply") {
    val id = long("id").primaryKey().bindTo { it.id }
    val repair = long("repair").bindTo { it.repair }
    val sender = varchar("sender").bindTo { it.sender }
    val type = int("type").bindTo { it.type }
    val content = text("content").bindTo { it.content }
    val time = datetime("time").bindTo { it.time }
}

interface ReplyEntity: Entity<ReplyEntity> {

    companion object : Entity.Factory<ReplyEntity>()

    var id: Long
    var repair: Long
    var sender: String
    var type: Int
    var content: String
    var time: LocalDateTime

    val value get() = Reply(this)
}
