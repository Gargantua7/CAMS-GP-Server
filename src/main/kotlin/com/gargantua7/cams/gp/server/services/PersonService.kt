package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.PersonDao
import com.gargantua7.cams.gp.server.exception.NotFoundException
import com.gargantua7.cams.gp.server.model.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Gargantua7
 */
@Service
@Transactional
class PersonService {

    @Autowired
    private lateinit var personDao: PersonDao

    fun selectPersonByUsername(username: String): Person {
        try {
            return personDao.selectPersonByUsername(username).value
        } catch (e: NoSuchElementException) {
            throw NotFoundException("Resource[$username] Not Found", e)
        }
    }

}
