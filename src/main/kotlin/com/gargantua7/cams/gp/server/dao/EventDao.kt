package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Event
import com.gargantua7.cams.gp.server.model.po.EventSigns
import com.gargantua7.cams.gp.server.model.po.Events
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.*
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

    fun selectEventById(id: Long): Event {
        return database.events.filter { it.id eq id }.single().value
    }

    fun selectAllEventId(): List<Long> {
        return database.events.toList().map { it.id }
    }

    fun createNewEventTable(eventId: Long): Boolean {
        val sql = """
            CREATE TABLE IF NOT EXISTS `event-${eventId}` (
                id VARCHAR(12) UNIQUE PRIMARY KEY NOT NULL UNIQUE,
                time DATETIME NOT NULL DEFAULT (NOW()),
                CONSTRAINT `event-${eventId}_person_username_fk`
                FOREIGN KEY (id) REFERENCES cams_gp.person (username)
            );
            """.trimMargin()
        database.useConnection {
            return it.prepareStatement(sql).execute()
        }
    }

    fun signUpForEvent(event: Long, id: String): Int {
        val table = EventSigns("event-$event")
        return database.insert(table) {
            set(table.id, id)
        }
    }

    fun selectCountSignAtEventByEventId(id: Long): Int {
        val table = EventSigns("event-$id")
        return database.sequenceOf(table).count()
    }
}
