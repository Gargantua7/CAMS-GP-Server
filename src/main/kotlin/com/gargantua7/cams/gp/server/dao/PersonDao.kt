package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.FullPerson
import com.gargantua7.cams.gp.server.model.dto.Person
import com.gargantua7.cams.gp.server.model.dto.SearchPersonModel
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
            .whereWithConditions { conditions ->
                person.username?.let { conditions += Persons.username eq it }
                person.username?.let { conditions += Persons.username eq it }
                person.name?.let { conditions += Persons.name like "$it%" }
                person.sex?.let { conditions += Persons.sex eq it }
                person.depId?.let { conditions += Persons.depId eq it }
                person.permissionLevel?.let { conditions += Persons.permissionLevel eq it }

            }.limit(page * 10, 10)
            .mapToFullPersonList()
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
            .mapToFullPersonList().single()
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
        return person.entity.let {
            val line = database.persons.update(it)
            if (it.phone.isNullOrEmpty() || it.wechat.isNullOrEmpty()) {
                database.update(Persons) { table ->
                    it.phone?.let { p -> set(table.phone, p.ifEmpty { null }) }
                    it.wechat?.let { w -> set(table.wechat, w.ifEmpty { null }) }
                    where { table.username eq it.username }
                }
            } else line
        }

    }

}
