package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Event
import com.gargantua7.cams.gp.server.model.po.*
import com.mysql.cj.util.LazyString
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDate

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


    // ----- statistics

    fun count(id: Long): Int {
        val table = EventSigns("event-$id")
        return database.from(table).select().totalRecords
    }

    fun sexGroup(id: Long): Map<String, Int> {
        val table = EventSigns("event-$id")
        val map = HashMap<String, Int>()
        database
            .from(table)
            .leftJoin(Persons, table.id eq Persons.username)
            .select()
            .forEach {
                val sex = if (it[Persons.sex] == true) "女" else "男"
                map[sex] = (map[sex] ?: 0) + 1
            }
        return map
    }

    fun timeGroup(id: Long): Map<String, Int> {
        val table = EventSigns("event-$id")
        val map = HashMap<String, Int>()
        database
            .from(table)
            .select()
            .forEach {
                val time = it[table.time]?: return@forEach
                val s = "${time.year}/${time.month.value}/${time.dayOfMonth} ${time.hour}时"
                map[s] = (map[s] ?: 0) + 1
            }
        return map
    }

    fun collageGroup(id: Long): Map<String, Int> {
        val table = EventSigns("event-$id")
        val map = HashMap<String, Int>()
        database
            .from(table)
            .leftJoin(Persons, table.id eq Persons.username)
            .leftJoin(Majors, Persons.majorId eq Majors.id)
            .leftJoin(Collages, Majors.collageId eq Collages.id)
            .select()
            .forEach {
                val collage = it[Collages.name] ?: return@forEach
                map[collage] = (map[collage] ?: 0) + 1
            }
        return map
    }

    fun majorGroup(id: Long): Map<String, Int> {
        val table = EventSigns("event-$id")
        val map = HashMap<String, Int>()
        database
            .from(table)
            .leftJoin(Persons, table.id eq Persons.username)
            .leftJoin(Majors, Persons.majorId eq Majors.id)
            .select()
            .forEach {
                val major = it[Majors.name] ?: return@forEach
                map[major] = (map[major] ?: 0) + 1
            }
        return map
    }
}
