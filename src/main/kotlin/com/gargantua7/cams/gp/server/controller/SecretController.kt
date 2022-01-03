package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.pojo.Secret
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable

/**
 * @author Gargantua7
 */
@RestController
@RequestMapping(produces=["application/json;charset=UTF-8"])
class SecretController {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/public/secret/log/in")
    fun login(secret: Secret): Serializable {
        val subject = SecurityUtils.getSubject()
        val token = UsernamePasswordToken(secret.username, secret.password)
        try {
            subject.login(token)
        } catch (_: AuthenticationException) {
            throw AuthorizedException("Username or Password Wrong")
        }
        token.isRememberMe = false
        return subject.getSession(true).id
    }

}
