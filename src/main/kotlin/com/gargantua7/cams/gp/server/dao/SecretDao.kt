package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.entity.SecretEntity
import com.gargantua7.cams.gp.server.entity.Secrets
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.first
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * @author Gargantua7
 */
@Repository
class SecretDao {

    @Autowired
    private lateinit var database: Database

    val Database.secrets get() = sequenceOf(Secrets)

    fun selectSecretByUsername(username: String): SecretEntity {
        return database.secrets.filter { it.username eq username }.first()
    }

    fun updateSecret(secret: SecretEntity): Int {
        return database.secrets.update(secret)
    }
}
