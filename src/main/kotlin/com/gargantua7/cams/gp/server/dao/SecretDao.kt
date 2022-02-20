package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Secret
import com.gargantua7.cams.gp.server.model.po.SecretEntity
import com.gargantua7.cams.gp.server.model.po.Secrets
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
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

    fun insertSecret(secret: Secret): Int {
        return database.secrets.add(secret.entity)
    }

    fun selectSecretByUsername(username: String): Secret {
        return database.secrets.filter { it.username eq username }.single().value
    }

    fun updateSecret(secret: SecretEntity): Int {
        return database.secrets.update(secret)
    }
}
