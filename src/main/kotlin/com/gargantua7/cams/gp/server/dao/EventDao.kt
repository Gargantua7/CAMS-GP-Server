package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Event
import com.gargantua7.cams.gp.server.model.po.Events
import org.ktorm.database.Database
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * @author Gargantua7
 */
@Repository
class EventDao {

    @Autowired
    private lateinit var database: Database

    private val Database.events get() = sequenceOf(Events)

    fun insertNewEvent(event: Event): Int {
        return database.events.add(event.entity)
    }

    fun createNewEventTable(name: String): Boolean {
        val sql = """CREATE TABLE IF NOT EXISTS `event-${name}` (
                id VARCHAR(12) UNIQUE PRIMARY KEY NOT NULL,
                time DATETIME NOT NULL 
                )
            """.trimMargin()
        database.useConnection {
            return it.prepareStatement(sql).execute()
        }
    }

}
