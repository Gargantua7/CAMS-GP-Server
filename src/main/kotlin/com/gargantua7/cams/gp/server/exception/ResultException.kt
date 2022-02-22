package com.gargantua7.cams.gp.server.exception

/**
 * @author Gargantua7
 */
sealed class ResultException(val code: Int, val msg: String, val info: String, cause: Throwable?) :
    RuntimeException("[$code: $msg] -> $info", cause) {
    override fun toString(): String {
        return "[$code: $msg] -> $info"
    }
}

open class BadRequestException(info: String, cause: Throwable? = null) : ResultException(400, "Bad Request", info, cause) {

    class RequestParamFormatException(info: String, cause: Throwable? = null) :
            BadRequestException("Wrong Request Parma Format: $info", cause)

}

open class AuthorizedException(info: String, cause: Throwable? = null) : ResultException(401, "Authorized", info, cause) {

    class InsufficientPermissionsException(cause: Throwable? = null) :
        AuthorizedException("Permissions Insufficient", cause)

}

class ForbiddenException(info: String, cause: Throwable? = null) : ResultException(403, "Forbidden", info, cause)

class NotFoundException(info: String, cause: Throwable? = null) : ResultException(404, "Not Found", info, cause)
