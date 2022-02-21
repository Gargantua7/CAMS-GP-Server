package com.gargantua7.cams.gp.server.controller.advice

import com.gargantua7.cams.gp.server.model.vo.Result
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * @author Gargantua7
 */
@RestControllerAdvice
class ResponseAdvice : ResponseBodyAdvice<Any> {

    @Autowired
    private lateinit var gson: Gson

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any {
        return when (body) {
            is Result -> body
            is List<*> -> Result.success(object {
                val list = body
            })
            else -> Result.success(body)
        }
    }
}
