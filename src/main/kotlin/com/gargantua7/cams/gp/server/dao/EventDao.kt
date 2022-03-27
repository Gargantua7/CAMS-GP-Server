package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Event
import com.gargantua7.cams.gp.server.model.po.EventSigns
import com.gargantua7.cams.gp.server.model.po.Events
import org.ktorm.database.Database
import org.ktorm.dsl.*
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

    fun selectAllEvent(page: Int): List<Event> {
        return database
            .from(Events)
            .select()
            .orderBy(Events.startTime.desc())
            .limit(page * 10, 10)
            .map {
                Event(
                    it[Events.id]!!,
                    it[Events.name]!!,
                    it[Events.content]!!,
                    it[Events.number]!!,
                    it[Events.location]!!,
                    it[Events.eventTime]!!,
                    it[Events.startTime]!!,
                    it[Events.endTime]!!
                )
            }
    }

    fun createNewEventTable(eventId: Long): Boolean {
        val sql = """
            CREATE TABLE IF NOT EXISTS `event-${eventId}` (
                id INT AUTO_INCREMENT UNIQUE PRIMARY KEY NOT NULL,
                username VARCHAR(12),
                time DATETIME NOT NULL DEFAULT (NOW()),
                CONSTRAINT `event-${eventId}_person_username_fk`
                FOREIGN KEY (username) REFERENCES cams_gp.person (username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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

    fun selectEventAllSign(id: Long, page: Int): List<String> {
        val table = EventSigns("event-$id")
        return database
            .from(table)
            .select()
            .limit(page * 10, 10)
            .map { it[table.id]!! }
    }
}
