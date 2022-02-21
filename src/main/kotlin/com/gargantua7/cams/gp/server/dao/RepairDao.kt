package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Repair
import com.gargantua7.cams.gp.server.model.po.Repairs
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

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

    fun selectAllRepairUUIDWithLimit(page: Int, requesterId: String = ""): List<String> {
        return database
            .from(Repairs)
            .select()
            .let {
                if (requesterId.isBlank()) it
                else it.where { (Repairs.private eq false) or (Repairs.initiator eq requesterId) or (Repairs.principal eq requesterId) }
            }.limit(page, 10)
            .orderBy(Repairs.updateTime.desc())
            .map { row -> row[Repairs.uuid]!! }
    }

    fun selectRepairByUUID(uuid: String): Repair {
        return database.repairs.filter { it.uuid eq uuid }.single().value
    }

    fun selectRepairUUIDListByKeyword(key: String, requesterId: String): List<String> {
        return database.repairs
            .filter { (it.title like "%$key%") or (it.content like "%$key%") }
            .let {
                if (requesterId.isBlank()) it
                else it.filter { (it.private eq false) or (Repairs.initiator eq requesterId) or (Repairs.principal eq requesterId) }
            }.map { it.uuid }
    }

    fun selectRepairUUIDWithUnassigned(): List<String> {
        return database.repairs.filter { it.principal.isNull() }.map { it.uuid }
    }

    fun selectRepairUUIDListByPerson(username: String, requesterId: String): List<String> {
        return database.repairs
            .filter { (Repairs.initiator eq username) or (Repairs.principal eq username) }
            .let {
                if (requesterId.isBlank()) it
                else it.filter { (it.private eq false) or (Repairs.initiator eq requesterId) or (Repairs.principal eq requesterId) }
            }
            .map { it.uuid }
    }

    fun assignPrincipleByUUID(uuid: String, principle: String): Int {
        return database.update(Repairs) {
            set(it.principal, principle)
            where { it.uuid eq uuid }
        }
    }

    fun changeStateByUUID(uuid: String, state: Boolean): Int {
        return database.update(Repairs) {
            set(it.state, state)
            where { it.uuid eq uuid }
        }
    }
}
