package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.SecretDao
import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.exception.NotFoundException
import com.gargantua7.cams.gp.server.model.dto.Secret
import org.apache.shiro.crypto.hash.Sha256Hash
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Gargantua7
 */
@Transactional
@Service
class SecretService {

    @Autowired
    private lateinit var secretDao: SecretDao

    fun selectSecretByUsername(username: String): Secret {
        try {
            return secretDao.selectSecretByUsername(username)
        } catch (e: NoSuchElementException) {
            throw NotFoundException("Secret[$username] Not Found", e)
        }
    }

    fun updateSecret(username: String, password: String, old: String) {
        val person = selectSecretByUsername(username)
        if (Sha256Hash(old, person.salt.toString(), 10).toString() != person.password)
            throw AuthorizedException("Wrong Password")
        val salt = Random().nextInt()
        val pwd = Sha256Hash(password, salt.toString(), 10).toString()
        if (secretDao.updateSecret(Secret(username, pwd, salt).getEntity()) == 0)
            throw RuntimeException("Data Exception: Found User[$username] but the password couldn't be updated")
    }

}
