package com.gargantua7.cams.gp.server.model.dto

import com.gargantua7.cams.gp.server.model.po.SecretEntity

/**
 * @author Gargantua7
 */
data class Secret(
    val username: String,
    var password: String,
    var salt: Int = 0
) {
    constructor(that: SecretEntity) : this(that.username, that.password, that.salt)

    fun getEntity() = SecretEntity {
        username = this@Secret.username
        password = this@Secret.password
        salt = this@Secret.salt
    }

}
