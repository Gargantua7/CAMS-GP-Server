package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.exception.BadRequestException
import com.gargantua7.cams.gp.server.matchPassword
import com.gargantua7.cams.gp.server.model.dto.Person
import com.gargantua7.cams.gp.server.model.dto.Secret
import com.gargantua7.cams.gp.server.model.vo.PasswordUpdateModel
import com.gargantua7.cams.gp.server.model.vo.SignUpModel
import com.gargantua7.cams.gp.server.services.SecretService
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.eis.MemorySessionDAO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

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

    @PostMapping("/public/secret/sign/up")
    fun signUp(@RequestBody info: SignUpModel) {
        info.require()
        val person = Person(info.username, info.name, info.majorId, 5, -1, info.phone, info.wechat)
        val salt = Random().nextInt()
        val secret = Secret(info.username, Sha256Hash(info.password, salt.toString(), 10).toString(), salt)
        secretService.insertSignUpPerson(person, secret)
    }

    @PostMapping("/public/secret/sign/in")
    fun signIn(@RequestBody secret: Secret): Any {
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

    @PostMapping("/private/secret/sign/out")
    fun signOut() {
        SecurityUtils.getSubject().logout()
    }

    @PostMapping("/private/secret/update")
    fun update(@RequestBody info: PasswordUpdateModel) {
        val (old, new) = info
        info.match()
        val username = SecurityUtils.getSubject().principal as String
        secretService.updateSecret(username, new, old)
        signOut()
    }

}
