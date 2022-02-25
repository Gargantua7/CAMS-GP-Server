package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.EventDao
import com.gargantua7.cams.gp.server.model.dto.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author Gargantua7
 */
@Service
class EventService {

    @Autowired
    private lateinit var eventDao: EventDao

    fun createNewEvent(event: Event) {
        eventDao.insertNewEvent(event)
    }

}
