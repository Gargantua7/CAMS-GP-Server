package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.entity.PersonEntity
import com.gargantua7.cams.gp.server.entity.Persons
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.first
import org.ktorm.entity.sequenceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * @author Gargantua7
 */
@Repository
class PersonDao {

    @Autowired
    private lateinit var database: Database

    val Database.persons get() = sequenceOf(Persons)

    fun selectPersonByUsername(username: String): PersonEntity {
        return database.persons.filter { it.username eq username }.first()
    }

}
