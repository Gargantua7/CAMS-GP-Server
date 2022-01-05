package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.exception.NotFoundException
import com.gargantua7.cams.gp.server.model.Secret
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.eis.MemorySessionDAO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Gargantua7
 */
@RestController
@RequestMapping(produces = ["application/json;charset=UTF-8"])
class SecretController {

    @Autowired
    private lateinit var sessionDao: MemorySessionDAO

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/public/secret/log/in")
    fun login(@RequestBody secret: Secret): Any {
        val subject = SecurityUtils.getSubject()
        val token = UsernamePasswordToken(secret.username, secret.password)

        var exist: Session? = null
        sessionDao.activeSessions.forEach { s ->
            logger.info(s.id.toString())
            if (s.getAttribute("username") == secret.username) {
                exist = s
            }
        }

        try {
            subject.login(token)
        } catch (ae: AuthenticationException) {
            throw AuthorizedException("Username or Password Wrong", ae)
        }

        token.isRememberMe = false
        subject.session.setAttribute("username", secret.username)
        exist?.let { it.timeout = 0 }
        return object {
            val session = subject.session.id
        }
    }

    @PostMapping("/private/secret/log/out")
    fun logout() {
        SecurityUtils.getSubject().logout()
    }

}
