package com.gargantua7.cams.gp.server.controller.advice

import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.exception.ForbiddenException
import com.gargantua7.cams.gp.server.exception.ResultException
import com.gargantua7.cams.gp.server.model.vo.Failure
import com.gargantua7.cams.gp.server.model.vo.Result
import org.apache.shiro.authz.UnauthenticatedException
import org.apache.shiro.authz.UnauthorizedException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

/**
 * @author Gargantua7
 */
@RestControllerAdvice
class ExceptionHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(ResultException::class)
    fun resultExceptionHandler(httpServletRequest: HttpServletRequest, e: ResultException): Failure {
        logger.warn("[${httpServletRequest.requestURI}] ${e.message}")
        return Result.failure(e)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun dataIntegrityViolationExceptionHandler(httpServletRequest: HttpServletRequest, e: DataIntegrityViolationException): Failure {
        logger.warn("[${httpServletRequest.requestURI}] ${e.message}")
        return Result.failure(ForbiddenException("Wrong Request Param", e))
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun duplicateKeyException(httpServletRequest: HttpServletRequest, e: DuplicateKeyException): Failure {
        logger.warn("[${httpServletRequest.requestURI}] ${e.message}")
        return Result.failure(ForbiddenException("Resource Already Exists", e))
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun unauthorizedExceptionHandler(httpServletRequest: HttpServletRequest, e: UnauthorizedException): Failure {
        logger.warn("[${httpServletRequest.requestURI}] ${e.message}")
        return Result.failure(AuthorizedException("Insufficient Permissions", e))
    }

    @ExceptionHandler(UnauthenticatedException::class)
    fun unauthenticatedExceptionHandler(httpServletRequest: HttpServletRequest, e: UnauthenticatedException): Failure {
        logger.warn("[${httpServletRequest.requestURI}] ${e.message}")
        return Result.failure(AuthorizedException("Unauthorized", e))
    }

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(httpServletRequest: HttpServletRequest, e: Exception): Failure {
        logger.error("[${httpServletRequest.requestURI}] Unknown Exception : ${e.message}", e)
        return Failure(500, "Internal Server Error", "Unknown Exception")
    }

}
