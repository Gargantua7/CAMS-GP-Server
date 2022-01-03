package com.gargantua7.cams.gp.server.exception

/**
 * @author Gargantua7
 */
sealed class ResultException(val code: Int, val msg: String, val info: String): RuntimeException("[$code: $msg] -> $info") {
    override fun toString(): String {
        return "[$code: $msg] -> $info"
    }
}

class BadRequestException(info: String): ResultException(400, "Bad Request", info)

class AuthorizedException(info: String): ResultException(401, "Authorized", info)

class NotFoundException(info: String): ResultException(404, "Not Found", info)
