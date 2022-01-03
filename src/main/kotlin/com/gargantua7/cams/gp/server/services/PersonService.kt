package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.PersonDao
import com.gargantua7.cams.gp.server.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author Gargantua7
 */
@Service
class PersonService {


    @Autowired
    private lateinit var personDao: PersonDao

    fun selectPersonByUsername(username: String) =
        personDao.selectPersonByUsername(username)?.value ?: throw NotFoundException("Resource[$username] Not Found")

}
