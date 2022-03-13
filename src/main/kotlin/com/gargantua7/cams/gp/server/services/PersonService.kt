package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.PersonDao
import com.gargantua7.cams.gp.server.exception.NotFoundException
import com.gargantua7.cams.gp.server.model.dto.FullPerson
import com.gargantua7.cams.gp.server.model.dto.Person
import com.gargantua7.cams.gp.server.model.vo.SearchPersonModel
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
            return personDao.selectPersonByUsername(username)
        } catch (e: NoSuchElementException) {
            throw NotFoundException("Person[$username] Not Found", e)
        }
    }

    fun selectPersonByConditional(model: SearchPersonModel, page: Int): List<FullPerson> {
        return personDao.selectPersonByConditional(model, page)
    }

    fun updatePersonByModel(person: Person) {
        personDao.updatePersonByModel(person)
    }
}
