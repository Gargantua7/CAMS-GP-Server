package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Repair
import com.gargantua7.cams.gp.server.model.dto.SearchRepairModel
import com.gargantua7.cams.gp.server.model.po.Repairs
import org.apache.shiro.SecurityUtils
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.single
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

    fun selectRepairByConditional(model: SearchRepairModel, page: Int): List<Repair> {
        return database.from(Repairs)
            .select()
            .let {
                if (checkPermission()) it
                else it.where {
                    val requesterId = SecurityUtils.getSubject().principal as String? ?: ""
                    (Repairs.private eq false) or (Repairs.initiator eq requesterId) or (Repairs.principal eq requesterId)
                }
            }
            .whereWithConditions { conditions ->
                model.id?.let { conditions += Repairs.id eq it }
                model.keyword?.let { conditions += Repairs.title like "%$it%" or (Repairs.content like "%$it%") }
                model.initiator?.let { conditions += Repairs.initiator eq it }
                model.state?.let { conditions += Repairs.state eq it }
                if (model.unassigned) {
                    conditions += Repairs.principal.isNull()
                } else model.principal?.let {
                    conditions += Repairs.principal eq it
                }
                conditions += Repairs.del eq false
            }
            .orderBy(Repairs.updateTime.desc())
            .limit(page * 10, 10)
            .map {
                Repair(
                    it[Repairs.id]!!,
                    it[Repairs.title]!!,
                    it[Repairs.content]!!,
                    it[Repairs.initiator]!!,
                    it[Repairs.principal],
                    it[Repairs.initTime]!!,
                    it[Repairs.updateTime]!!,
                    it[Repairs.state]!!,
                    it[Repairs.private]!!
                )
            }
    }

    fun selectRepairByID(id: Long): Repair {
        return database.repairs.filter { it.id eq id }.single().value
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

    fun deleteRepairByID(id: Long): Int {
        return database.update(Repairs) {
            set(it.del, true)
            where { it.id eq id }
        }
    }
}
