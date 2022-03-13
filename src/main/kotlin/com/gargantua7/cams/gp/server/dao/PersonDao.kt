package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.FullPerson
import com.gargantua7.cams.gp.server.model.dto.Person
import com.gargantua7.cams.gp.server.model.po.*
import com.gargantua7.cams.gp.server.model.vo.SearchPersonModel
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
        return database.persons.add(person.entity)
    }

    fun selectPersonByUsername(username: String): Person {
        return database.persons.filter { it.username eq username }.single().value
    }

    fun selectPersonByConditional(person: SearchPersonModel, page: Int): List<FullPerson> {
        return database
            .from(Persons)
            .leftJoin(Departments, Persons.depId eq Departments.id)
            .leftJoin(Majors, Persons.majorId eq Majors.id)
            .leftJoin(Collages, Majors.collageId eq Collages.id)
            .leftJoin(Permissions, Persons.permissionLevel eq Permissions.level)
            .select()
            .run { person.username?.let { where { Persons.username eq it } } ?: this }
            .run { person.name?.let { where { Persons.name like "$it%" } } ?: this }
            .run { person.sex?.let { where { Persons.sex eq it } } ?: this }
            .run { person.depId?.let { where { Persons.depId eq it } } ?: this }
            .run { person.permissionLevel?.let { where { Persons.permissionLevel eq it } } ?: this }
            .limit(page * 10, 10)
            .mapToFullPersonList()
    }

    private fun Query.mapToFullPersonList(): List<FullPerson> {
        return map { row ->
            FullPerson(
                username = row[Persons.username]!!,
                name = row[Persons.name]!!,
                sex = row[Persons.sex]!!,
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
        return database.persons.update(person.entity)
    }

}
