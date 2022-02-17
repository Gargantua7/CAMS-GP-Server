package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.exception.ForbiddenException
import com.gargantua7.cams.gp.server.model.dto.Person
import com.gargantua7.cams.gp.server.model.vo.FullPersonModel
import com.gargantua7.cams.gp.server.model.vo.PersonInfoUpdateModel
import com.gargantua7.cams.gp.server.services.PersonService
import org.apache.shiro.SecurityUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author Gargantua7
 */
@RestController
@RequestMapping(produces = ["application/json;charset=UTF-8"])
class PersonController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var personService: PersonService

    @GetMapping("/private/person/info/search/me")
    fun searchMe() = searchById(SecurityUtils.getSubject().principal as String)

    @GetMapping("/public/person/info/search/id/{username}")
    fun searchById(@PathVariable username: String): FullPersonModel {
        val requesterId = SecurityUtils.getSubject().principal as String?
        val queried = personService.selectPersonByUsername(username)
        if (username != requesterId) {
            val requester = requesterId?.let { personService.selectPersonByUsername(it) }
            if ((requester?.permissionLevel ?: -99) <= queried.permissionLevel) {
                return personService.toFullInfo(queried, true)
            }
        }
        return personService.toFullInfo(queried)
    }

    @GetMapping("/public/person/info/search/name/{name}")
    fun searchByName(@PathVariable name: String): Any {
        val requesterId = SecurityUtils.getSubject().principal as String?
        val requester = requesterId?.let { personService.selectPersonByUsername(it) }
        val requesterPermission = requester?.permissionLevel ?: -99
        val list = personService.selectPersonListByName(name).map {
            personService.toFullInfo(it, requesterId != it.username && requesterPermission <= it.permissionLevel)
        }
        return object {
            val list = list
        }
    }

    @PostMapping("/private/person/info/update")
    fun update(@RequestBody model: PersonInfoUpdateModel) {
        model.require()
        val requesterId = SecurityUtils.getSubject().principal as String
        val updated = personService.selectPersonByUsername(model.username)
        if (requesterId != model.username) {
            val requester = personService.selectPersonByUsername(requesterId)
            if (requester.permissionLevel <= updated.permissionLevel)
                throw ForbiddenException("Insufficient Permissions")
        }
        val person = Person(
            updated.username,
            model.name ?: updated.name,
            model.majorId ?: updated.majorId,
            updated.depId,
            updated.permissionLevel,
            model.phone ?: updated.phone,
            model.wechat ?: updated.wechat
        )
        personService.updatePersonByModel(person)
    }
}
