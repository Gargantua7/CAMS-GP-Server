package com.gargantua7.cams.gp.server.model.po

import com.gargantua7.cams.gp.server.model.dto.Repair
import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
object Repairs: Table<RepairEntity>("repair") {
    val id = long("id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
    val content = text("content").bindTo { it.content }
    val initiator = varchar("initiator").bindTo { it.initiator }
    val principal = varchar("principal").bindTo { it.principal }
    val initTime = datetime("init_time").bindTo { it.initTime }
    val updateTime = datetime("update_time").bindTo { it.updateTime }
    val state = boolean("state").bindTo { it.state }
    val private = boolean("private").bindTo { it.private }
    val del = boolean("del")
}

interface RepairEntity: Entity<RepairEntity> {

    companion object : Entity.Factory<RepairEntity>()

    var id: Long
    var title: String
    var content: String
    var initiator: String
    var principal: String?
    var initTime: LocalDateTime
    var updateTime: LocalDateTime
    var state: Boolean
    var private: Boolean

    val value get() = Repair(this)
}
