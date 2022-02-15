package com.gargantua7.cams.gp.server.controller.advice

import com.gargantua7.cams.gp.server.exception.ResultException
import com.gargantua7.cams.gp.server.model.vo.Failure
import com.gargantua7.cams.gp.server.model.vo.Result
import org.slf4j.LoggerFactory
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

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(httpServletRequest: HttpServletRequest, e: Exception): Failure {
        logger.error("[${httpServletRequest.requestURI}] Unknown Exception : ${e.message}", e)
        return Failure(500, "Internal Server Error", "Unknown Exception")
    }
}
