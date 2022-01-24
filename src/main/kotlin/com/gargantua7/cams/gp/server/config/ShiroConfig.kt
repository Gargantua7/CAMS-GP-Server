package com.gargantua7.cams.gp.server.config

import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.model.vo.Result
import com.gargantua7.cams.gp.server.services.PersonService
import com.gargantua7.cams.gp.server.services.SecretService
import com.google.gson.Gson
import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.SimpleAuthenticationInfo
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.authc.credential.HashedCredentialsMatcher
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.session.mgt.eis.MemorySessionDAO
import org.apache.shiro.session.mgt.eis.SessionDAO
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.util.ByteSource
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.web.servlet.ShiroHttpServletRequest
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager
import org.apache.shiro.web.util.WebUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.Serializable
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

/**
 * @author Gargantua7
 */
@Configuration
class ShiroConfig {

    @Bean
    fun shiroFilterFactory(@Autowired webSecurityManager: DefaultWebSecurityManager) = ShiroFilterFactoryBean().apply {
        securityManager = webSecurityManager
        filterChainDefinitionMap = mapOf(
            "/public/**" to "anon",
            "/private/**" to "authc"
        )
        filters["authc"] = object : FormAuthenticationFilter() {
            override fun onAccessDenied(request: ServletRequest?, response: ServletResponse?): Boolean {
                val httpServletResponse = response as HttpServletResponse
                httpServletResponse.status = 200
                httpServletResponse.contentType = "application/json;charset=utf-8"

                val out = httpServletResponse.writer
                val json = Gson().toJson(Result.failure(AuthorizedException("Unauthorized")))
                out.println(json)
                return false
            }
        }
    }


    @Bean
    fun webSecurityManager(@Autowired authorizingRealm: AuthorizingRealm, @Autowired sessionDAO: SessionDAO) =
        DefaultWebSecurityManager().apply {
            authorizingRealm.credentialsMatcher = HashedCredentialsMatcher().apply {
                hashAlgorithmName = "sha-256"
                hashIterations = 10
            }
            setRealm(authorizingRealm)
            sessionManager = object : DefaultWebSessionManager() {

                override fun getSessionId(request: ServletRequest, response: ServletResponse): Serializable? {
                    val session = WebUtils.toHttp(request).getHeader("session")
                    return if (session == null || session.isEmpty()) super.getSessionId(request, response)
                    else request.run {
                        setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "request_header")
                        setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, session)
                        setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, true)
                        session
                    }
                }
            }.apply {
                isSessionIdCookieEnabled = false
                isSessionIdUrlRewritingEnabled = false
                globalSessionTimeout = 3600000L
                this.sessionDAO = sessionDAO
            }
            rememberMeManager = null
        }

    @Bean
    fun sessionDao() = MemorySessionDAO()


    @Bean
    fun realm(@Autowired personService: PersonService, @Autowired secretService: SecretService) =
        object : AuthorizingRealm() {
            override fun doGetAuthenticationInfo(token: AuthenticationToken): AuthenticationInfo? {
                val username = (token as UsernamePasswordToken).username
                val secret = secretService.selectSecretByUsername(username)
                val salt = ByteSource.Util.bytes(secret.salt.toString())

                return SimpleAuthenticationInfo(username, secret.password, salt, name)
            }

            override fun doGetAuthorizationInfo(principals: PrincipalCollection): AuthorizationInfo? {
                return null
            }

        }

}
