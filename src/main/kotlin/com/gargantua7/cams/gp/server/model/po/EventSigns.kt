package com.gargantua7.cams.gp.server.model.po

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
class EventSigns(name: String): Table<EventSignEntity>(name) {

    val id = varchar("id").primaryKey().bindTo { it.id }
    val time = datetime("time").bindTo { it.time }

}

interface EventSignEntity: Entity<EventSignEntity> {

    companion object: Entity.Factory<EventEntity>()

    var id: String
    var time: LocalDateTime


}
