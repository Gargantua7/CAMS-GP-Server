package com.gargantua7.cams.gp.server.model.dto

import com.gargantua7.cams.gp.server.model.po.SecretEntity

/**
 * @author Gargantua7
 */
data class Secret(
    val username: String,
    val password: String,
    val salt: Int = 0
) {
    constructor(that: SecretEntity) : this(that.username, that.password, that.salt)

    val entity
        get() = SecretEntity {
            username = this@Secret.username
            password = this@Secret.password
            salt = this@Secret.salt
        }

}
