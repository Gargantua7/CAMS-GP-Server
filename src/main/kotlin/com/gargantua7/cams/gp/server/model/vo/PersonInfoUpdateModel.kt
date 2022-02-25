package com.gargantua7.cams.gp.server.model.vo

import com.gargantua7.cams.gp.server.exception.BadRequestException
import com.gargantua7.cams.gp.server.matchPhone
import com.gargantua7.cams.gp.server.matchUsername
import com.gargantua7.cams.gp.server.matchWechat

/**
 * @author Gargantua7
 */
data class PersonInfoUpdateModel(
    val username: String,
    val majorId: String?,
    val phone: String?,
    val wechat: String?
) {
    init {
        if (!matchUsername(username) || !matchPhone(phone) || !matchWechat(wechat)) {
            throw BadRequestException.RequestParamFormatException("Wrong Request Param Format")
        }
        require(!matchUsername(username)) { "Invalid Username" }
        require(!matchPhone(phone)) { "Invalid Phone Number" }
        require(!matchWechat(wechat)) { "Invalid Wechat ID" }
    }
}
