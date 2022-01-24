package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.InfoDao
import com.gargantua7.cams.gp.server.dao.PersonDao
import com.gargantua7.cams.gp.server.exception.NotFoundException
import com.gargantua7.cams.gp.server.model.dto.Person
import com.gargantua7.cams.gp.server.model.vo.PersonInfo
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
    private lateinit var infoDao: InfoDao

    @Autowired
    private lateinit var personDao: PersonDao

    fun selectPersonByUsername(username: String): Person {
        try {
            return personDao.selectPersonByUsername(username)
        } catch (e: NoSuchElementException) {
            throw NotFoundException("Person[$username] Not Found", e)
        }
    }

    fun toFullInfo(person: Person): PersonInfo {
        val dep = infoDao.selectDepById(person.depId)
        val major = infoDao.selectMajorById(person.majorId)
        val collage = infoDao.selectCollageById(major.collageId)
        val permission = infoDao.selectPermissionByLevel(person.permissionLevel)
        return PersonInfo(
            person.username,
            person.name,
            major.name,
            collage.name,
            dep.name,
            person.permissionLevel,
            permission.title,
            person.phone,
            person.wechat
        )
    }
}
