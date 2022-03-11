package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.model.dto.Event
import com.gargantua7.cams.gp.server.model.vo.NewEventModel
import com.gargantua7.cams.gp.server.services.EventService
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author Gargantua7
 */
@RestController
@RequestMapping(produces = ["application/json;charset=UTF-8"])
class EventController {

    @Autowired
    private lateinit var eventService: EventService

    @RequiresAuthentication
    @RequiresPermissions("d_Club")
    @PostMapping("/event/create")
    fun createNewEvent(@RequestBody model: NewEventModel) {
        eventService.createNewEvent(
            Event(
                name = model.name,
                content = model.content,
                number = model.number,
                location = model.location,
                eventTime = model.eventTime,
                startTime = model.startTime,
                endTime = model.endTime
            )
        )
    }
}
