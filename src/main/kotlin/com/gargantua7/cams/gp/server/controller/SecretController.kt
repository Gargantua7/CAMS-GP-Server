package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.exception.BadRequestException
import com.gargantua7.cams.gp.server.model.dto.Secret
import com.gargantua7.cams.gp.server.services.SecretService
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.eis.MemorySessionDAO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author Gargantua7
 */
@RestController
@RequestMapping(produces = ["application/json;charset=UTF-8"])
class SecretController {

    @Autowired
    private lateinit var secretService: SecretService

    @Autowired
    private lateinit var sessionDao: MemorySessionDAO

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/public/secret/log/in")
    fun login(@RequestBody secret: Secret): Any {
        val subject = SecurityUtils.getSubject()
        val token = UsernamePasswordToken(secret.username, secret.password)

        val sessionList = ArrayList<Session>()
        sessionDao.activeSessions.forEach { s ->
            if (s.getAttribute("username") == secret.username) {
                sessionList.add(s)
            }
        }

        try {
            subject.login(token)
        } catch (ae: AuthenticationException) {
            throw AuthorizedException("Username or Password Wrong", ae)
        }

        token.isRememberMe = false
        subject.session.setAttribute("username", secret.username)
        sessionList.forEach { it.timeout = 0 }
        return object {
            val session = subject.session.id
        }
    }

    @PostMapping("/private/secret/log/out")
    fun logout() {
        SecurityUtils.getSubject().logout()
    }

    @PostMapping("/private/secret/update")
    fun update(@RequestParam old: String, @RequestParam new: String) {
        if (!"^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9~!@#\$%^&*]{8,16}\$".toRegex().matches(new))
            throw BadRequestException("Wrong Password Format")
        val username = SecurityUtils.getSubject().principal as String
        secretService.updateSecret(username, new, old)
        logout()
    }

}
