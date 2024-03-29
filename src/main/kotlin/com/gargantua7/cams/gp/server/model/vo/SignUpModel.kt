package com.gargantua7.cams.gp.server.model.vo

import com.gargantua7.cams.gp.server.exception.BadRequestException
import com.gargantua7.cams.gp.server.matchPassword
import com.gargantua7.cams.gp.server.matchPhone
import com.gargantua7.cams.gp.server.matchUsername
import com.gargantua7.cams.gp.server.matchWechat

/**
 * @author Gargantua7
 */
data class SignUpModel(
    val username: String,
    val password: String,
    val name: String,
    val sex: Boolean,
    val majorId: String,
    val phone: String?,
    val wechat: String?
) {

    fun require() {
        if (!matchUsername(username) || !matchPassword(password) || !matchPhone(phone) || !matchWechat(wechat)) {
                throw BadRequestException.RequestParamFormatException("Wrong Request Param Format")
        }
    }

}
