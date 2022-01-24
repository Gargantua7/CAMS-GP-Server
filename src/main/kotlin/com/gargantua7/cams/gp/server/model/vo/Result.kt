package com.gargantua7.cams.gp.server.model.vo

import com.gargantua7.cams.gp.server.exception.ResultException

/**
 * @author Gargantua7
 */
sealed class Result(val code: Int, val msg: String) {

    companion object {
        fun <T> success(data: T) = Success(data)

        fun failure(exception: ResultException) = Failure(exception.code, exception.msg, exception.info)
    }

}

class Success<T>(val data: T) : Result(0, "OK")

class Failure(code: Int, msg: String, val info: String) : Result(code, msg)
