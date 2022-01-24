package com.gargantua7.cams.gp.server.model.po

import com.gargantua7.cams.gp.server.model.dto.Secret
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar


/**
 * @author Gargantua7
 */
object Secrets : Table<SecretEntity>("secret") {

    val username = varchar("username").primaryKey().bindTo { it.username }
    val password = varchar("password").bindTo { it.password }
    val salt = int("salt").bindTo { it.salt }

}

interface SecretEntity : Entity<SecretEntity> {

    companion object : Entity.Factory<SecretEntity>()

    var username: String
    var password: String
    var salt: Int

    val value get() = Secret(this)
}
