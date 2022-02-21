package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.model.dto.Person
import com.gargantua7.cams.gp.server.model.vo.FullPersonModel
import com.gargantua7.cams.gp.server.model.vo.PersonInfoUpdateModel
import com.gargantua7.cams.gp.server.services.PersonService
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.annotation.RequiresAuthentication
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

    @RequiresAuthentication
    @GetMapping("/person/info/search/me")
    fun searchMe() = searchById(SecurityUtils.getSubject().principal as String)

    @GetMapping("/person/info/search/id/{username}")
    fun searchById(@PathVariable username: String): FullPersonModel {
        val requesterId = SecurityUtils.getSubject().principal as String?
        val queried = personService.selectFullPersonByUsername(username)
        if (username != requesterId) {
            val requester = requesterId?.let { personService.selectPersonByUsername(it) }
            if ((requester?.permissionLevel ?: -99) <= queried.permission) {
                return queried.toVo(true)
            }
        }
        return queried.toVo()
    }

    @GetMapping("/person/info/search/name/{name}")
    fun searchByName(@PathVariable name: String): List<FullPersonModel> {
        val requesterId = SecurityUtils.getSubject().principal as String?
        val requester = requesterId?.let { personService.selectPersonByUsername(it) }
        val requesterPermission = requester?.permissionLevel ?: -99
        val list = personService.selectFullPersonListByName(name).map {
            it.toVo(requesterId != it.username && requesterPermission <= it.permission)
        }
        return list
    }

    @RequiresAuthentication
    @PostMapping("/person/info/update")
    fun update(@RequestBody model: PersonInfoUpdateModel) {
        model.require()
        val requesterId = SecurityUtils.getSubject().principal as String
        val updated = personService.selectPersonByUsername(model.username)
        if (requesterId != model.username) {
            val requester = personService.selectPersonByUsername(requesterId)
            if (requester.permissionLevel <= updated.permissionLevel)
                throw AuthorizedException("Insufficient Permissions")
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
