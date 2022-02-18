package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.FullPerson
import com.gargantua7.cams.gp.server.model.dto.Person
import com.gargantua7.cams.gp.server.model.po.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
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

    fun selectFullPersonByUsername(username: String): FullPerson {
        return database
            .from(Persons)
            .leftJoin(Departments, Persons.depId eq Departments.id)
            .leftJoin(Majors, Persons.majorId eq Majors.id)
            .leftJoin(Collages, Majors.collageId eq Collages.id)
            .leftJoin(Permissions, Persons.permissionLevel eq Permissions.level)
            .select()
            .where { Persons.username eq username }
            .mapToFullPersonList().first()
    }

    fun selectFullPersonListByName(name: String): List<FullPerson> {
        return database
            .from(Persons)
            .leftJoin(Departments, Persons.depId eq Departments.id)
            .leftJoin(Majors, Persons.majorId eq Majors.id)
            .leftJoin(Collages, Majors.collageId eq Collages.id)
            .leftJoin(Permissions, Persons.permissionLevel eq Permissions.level)
            .select().where { Persons.name like "$name%" }
            .orderBy(Persons.depId.asc(), Persons.permissionLevel.desc())
            .mapToFullPersonList()
    }

    private fun Query.mapToFullPersonList(): List<FullPerson> {
        return map { row ->
            FullPerson(
                username = row[Persons.username]!!,
                name = row[Persons.name]!!,
                major = row[Majors.name]!!,
                collage = row[Collages.name]!!,
                majorId = row[Persons.majorId]!!,
                dep = row[Departments.name]!!,
                depId = row[Persons.depId]!!,
                permission = row[Persons.permissionLevel]!!,
                title = row[Permissions.title]!!,
                phone = row[Persons.phone],
                wechat = row[Persons.wechat]
            )
        }
    }

    fun updatePersonByModel(person: Person): Int {
        return database.persons.update(person.getEntity())
    }

}
