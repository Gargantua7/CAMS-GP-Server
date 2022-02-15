package com.gargantua7.cams.gp.server.model.vo

import com.gargantua7.cams.gp.server.exception.BadRequestException
import com.gargantua7.cams.gp.server.matchPassword

/**
 * @author Gargantua7
 */
data class PasswordUpdateModel(val old: String, val new: String) {
    fun match() {
        if (!matchPassword(new))
            throw BadRequestException("Wrong Password Format")
    }
}
