package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.SecretDao
import com.gargantua7.cams.gp.server.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author Gargantua7
 */
@Service
class SecretService {


    @Autowired
    private lateinit var secretDao: SecretDao

    fun selectSecretByUsername(username: String) =
        secretDao.selectSecretByUsername(username)?.value ?: throw NotFoundException("Resource[$username] Not Found")

}
