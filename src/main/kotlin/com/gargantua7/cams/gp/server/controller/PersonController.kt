package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.model.dto.Person
import com.gargantua7.cams.gp.server.model.vo.FullPersonModel
import com.gargantua7.cams.gp.server.model.vo.PersonInfoUpdateModel
import com.gargantua7.cams.gp.server.model.dto.SearchPersonModel
import com.gargantua7.cams.gp.server.services.PersonService
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author Gargantua7
 */
@RestController
@RequestMapping(produces = ["application/json;charset=UTF-8"])
class PersonController {

    @Autowired
    private lateinit var personService: PersonService

    @GetMapping("/person/info/search/{page}")
    fun searchByConditional(@RequestBody model: SearchPersonModel, @PathVariable page: Int): List<FullPersonModel> {
        val requesterId = SecurityUtils.getSubject().principal as String?
        val requester = requesterId?.let { personService.selectPersonByUsername(it) }
        val requesterPermission = requester?.permissionLevel ?: -99
        return personService.selectPersonByConditional(model, page).map {
            it.toVo(requesterId != it.username && requesterPermission <= it.permission)
        }
    }

    @RequiresAuthentication
    @PostMapping("/person/info/update")
    fun update(@RequestBody model: PersonInfoUpdateModel) {
        val requesterId = SecurityUtils.getSubject().principal as String
        val updated = personService.selectPersonByUsername(model.username)
        if (requesterId != model.username) {
            val requester = personService.selectPersonByUsername(requesterId)
            if (requester.permissionLevel <= updated.permissionLevel)
                throw AuthorizedException.InsufficientPermissionsException()
        }
        val person = Person(
            updated.username,
            updated.name,
            updated.sex,
            model.majorId ?: updated.majorId,
            updated.depId,
            updated.permissionLevel,
            model.phone ?: updated.phone,
            model.wechat ?: updated.wechat
        )
        personService.updatePersonByModel(person)
    }
}
