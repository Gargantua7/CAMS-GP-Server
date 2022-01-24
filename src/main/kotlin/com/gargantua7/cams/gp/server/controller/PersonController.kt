package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.model.vo.PersonInfo
import com.gargantua7.cams.gp.server.services.PersonService
import org.apache.shiro.SecurityUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Gargantua7
 */
@RestController
@RequestMapping("/private/person", produces = ["application/json;charset=UTF-8"])
class PersonController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var personService: PersonService

    @GetMapping("/info/search/me")
    fun search() = search(SecurityUtils.getSubject().principal as String)

    @GetMapping("/info/search/{username}")
    fun search(@PathVariable username: String): PersonInfo {
        return personService.toFullInfo(personService.selectPersonByUsername(username))
    }

}
