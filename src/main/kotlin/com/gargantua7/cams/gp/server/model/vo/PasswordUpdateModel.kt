package com.gargantua7.cams.gp.server.model.vo

import com.gargantua7.cams.gp.server.matchPassword

/**
 * @author Gargantua7
 */
data class PasswordUpdateModel(val old: String, val new: String) {
    init {
        require(!matchPassword(new)) { "Wrong Password Format" }
    }
}
