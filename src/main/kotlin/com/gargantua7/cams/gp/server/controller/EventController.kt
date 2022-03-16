package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.model.dto.Event
import com.gargantua7.cams.gp.server.model.dto.FullPerson
import com.gargantua7.cams.gp.server.model.dto.Message
import com.gargantua7.cams.gp.server.model.vo.NewEventModel
import com.gargantua7.cams.gp.server.services.EventService
import com.gargantua7.cams.gp.server.services.MsgService
import org.apache.shiro.SecurityUtils
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
    private lateinit var msgService: MsgService

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

    @GetMapping("/event/list/{page}")
    fun getAllEventList(@PathVariable page: Int) = eventService.selectAllEvent(page)

    @GetMapping("/event/{eventId}/get")
    fun getEventById(@PathVariable eventId: Long) = eventService.selectEventById(eventId)

    @RequiresAuthentication
    @PostMapping("/event/{eventId}/sign")
    fun signUpForEvent(@PathVariable eventId: Long) {
        eventService.signUpForEvent(eventId)
        msgService.sendMsg(Message.Event(SecurityUtils.getSubject().principal as String, eventId))
    }

    @RequiresAuthentication
    @RequiresPermissions("d_Club")
    @GetMapping("/event/{id}/list/{page}")
    fun getSignList(@PathVariable id: Long, @PathVariable page: Int): List<FullPerson> {
        return eventService.selectEventAllSign(id, page)
    }
}
