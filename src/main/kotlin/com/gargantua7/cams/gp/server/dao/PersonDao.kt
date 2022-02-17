package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Person
import com.gargantua7.cams.gp.server.model.po.Persons
import org.ktorm.database.Database
import org.ktorm.dsl.asc
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.like
import org.ktorm.entity.*
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

    fun insertPerson(person: Person): Int {
        return database.persons.add(person.getEntity())
    }

    fun selectPersonByUsername(username: String): Person {
        return database.persons.filter { it.username eq username }.first().value
    }

    fun selectPersonListByName(name: String): List<Person> {
        return database.persons.filter { it.name like "$name%" }
            .sortedBy({ it.depId.asc() }, { it.permissionLevel.desc() })
            .map { it.value }
    }

    fun updatePersonByModel(person: Person): Int {
        return database.persons.update(person.getEntity())
    }

}
