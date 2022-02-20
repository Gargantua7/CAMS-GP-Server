package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Collage
import com.gargantua7.cams.gp.server.model.dto.Department
import com.gargantua7.cams.gp.server.model.dto.Major
import com.gargantua7.cams.gp.server.model.dto.Permission
import com.gargantua7.cams.gp.server.model.po.Collages
import com.gargantua7.cams.gp.server.model.po.Departments
import com.gargantua7.cams.gp.server.model.po.Majors
import com.gargantua7.cams.gp.server.model.po.Permissions
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * @author Gargantua7
 */
@Repository
class InfoDao {

    @Autowired
    private lateinit var database: Database

    val Database.majors get() = sequenceOf(Majors)
    val Database.collages get() = sequenceOf(Collages)
    val Database.deps get() = sequenceOf(Departments)
    val Database.permissions get() = sequenceOf(Permissions)

    fun selectAllDep(): List<Department> {
        return database.deps.toList()
    }

    fun selectDepById(id: Int): Department {
        return database.deps.filter { it.id eq id }.single()
    }

    fun selectMajorById(id: String): Major {
        return database.majors.filter { it.id eq id }.single()
    }

    fun selectMajorListByCollageId(collageId: String): List<Major> {
        return database.majors.filter { it.collageId eq collageId }.toList()
    }

    fun selectAllCollage(): List<Collage> {
        return database.collages.toList()
    }

    fun selectCollageById(id: String): Collage {
        return database.collages.filter { it.id eq id }.single()
    }

    fun selectPermissionByLevel(level: Int): Permission {
        return database.permissions.filter { it.level eq level }.single()
    }
}
