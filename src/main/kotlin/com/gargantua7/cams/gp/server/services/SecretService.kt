package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.SecretDao
import com.gargantua7.cams.gp.server.exception.NotFoundException
import com.gargantua7.cams.gp.server.model.Secret
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Gargantua7
 */
@Service
@Transactional
class SecretService {

    @Autowired
    private lateinit var secretDao: SecretDao

    fun selectSecretByUsername(username: String): Secret {
        try {
            return secretDao.selectSecretByUsername(username).value
        } catch (e: NoSuchElementException) {
            throw NotFoundException("Resource[$username] Not Found", e)
        }
    }

}
