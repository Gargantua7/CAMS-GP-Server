package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.EventDao
import com.gargantua7.cams.gp.server.dao.PersonDao
import com.gargantua7.cams.gp.server.exception.BadRequestException
import com.gargantua7.cams.gp.server.exception.ForbiddenException
import com.gargantua7.cams.gp.server.model.dto.Event
import com.gargantua7.cams.gp.server.model.dto.FullPerson
import org.apache.shiro.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
@Service
class EventService {

    @Autowired
    private lateinit var personDao: PersonDao

    @Autowired
    private lateinit var eventDao: EventDao

    fun createNewEvent(event: Event) {
        eventDao.insertNewEvent(event)
        eventDao.createNewEventTable(event.id)
    }

    fun selectEventById(eventId: Long) = eventDao.selectEventById(eventId)

    fun selectAllEvent(page: Int) = eventDao.selectAllEvent(page)

    fun signUpForEvent(eventId: Long) {
        val event = eventDao.selectEventById(eventId)
        if (LocalDateTime.now() !in event.startTime..event.endTime)
            throw BadRequestException.CurrentlyNotAllowedException()
        if (event.number <= eventDao.selectCountSignAtEventByEventId(eventId))
            throw ForbiddenException("Event signed is full")
        eventDao.signUpForEvent(eventId, SecurityUtils.getSubject().principal as String)
    }

    fun selectEventAllSign(id: Long, page: Int): List<FullPerson> {
        return eventDao.selectEventAllSign(id, page).map {
            personDao.selectFullPersonByUsername(it)
        }
    }

}
