package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Repair
import com.gargantua7.cams.gp.server.model.po.Repairs
import org.apache.shiro.SecurityUtils
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
@Repository
class RepairDao {

    @Autowired
    private lateinit var database: Database

    private val Database.repairs get() = sequenceOf(Repairs)

    fun insert(repair: Repair): Int {
        return database.repairs.add(repair.entity)
    }

    fun selectAllRepairID(): List<Long> {
        return database
            .from(Repairs)
            .select()
            .let {
                if (checkPermission()) it
                else it.where {
                    val requesterId = SecurityUtils.getSubject().principal as String? ?: ""
                    (Repairs.private eq false) or (Repairs.initiator eq requesterId) or (Repairs.principal eq requesterId)
                }
            }
            .orderBy(Repairs.updateTime.desc())
            .map { row -> row[Repairs.id]!! }
    }

    fun selectRepairByID(id: Long): Repair {
        return database.repairs.filter { it.id eq id }.single().value
    }

    fun selectRepairIDListByKeyword(key: String): List<Long> {
        return database.repairs
            .filter { (it.title like "%$key%") or (it.content like "%$key%") }
            .let { it ->
                if (checkPermission()) it
                else it.filter {
                    val requesterId = SecurityUtils.getSubject().principal as String? ?: ""
                    (it.private eq false) or (Repairs.initiator eq requesterId) or (Repairs.principal eq requesterId)
                }
            }.map { it.id }
    }

    fun selectRepairIDWithUnassigned(): List<Long> {
        return database.repairs.filter { it.principal.isNull() }.map { it.id }
    }

    fun selectRepairIDListByPerson(username: String): List<Long> {
        return database.repairs
            .filter { (Repairs.initiator eq username) or (Repairs.principal eq username) }
            .let { it ->
                if (checkPermission()) it
                else it.filter {
                    val requesterId = SecurityUtils.getSubject().principal as String? ?: ""
                    (it.private eq false) or (Repairs.initiator eq requesterId) or (Repairs.principal eq requesterId)
                }
            }
            .map { it.id }
    }

    fun assignPrincipleByID(id: Long, principle: String): Int {
        return database.update(Repairs) {
            set(it.principal, principle)
            where { it.id eq id }
        }
    }

    fun changeStateByID(id: Long, state: Boolean): Int {
        return database.update(Repairs) {
            set(it.state, state)
            where { it.id eq id }
        }
    }

    fun refreshUpdateTime(id: Long): Int {
        return database.update(Repairs) {
            set(it.updateTime, LocalDateTime.now())
            where { it.id eq id }
        }
    }

    private fun checkPermission(): Boolean {
        val subject = SecurityUtils.getSubject()
        return subject.hasRole("dep:1") && subject.isPermitted("Dep")
    }
}
