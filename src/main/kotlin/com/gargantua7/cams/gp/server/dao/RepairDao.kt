package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Repair
import com.gargantua7.cams.gp.server.model.po.Repairs
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.first
import org.ktorm.entity.sequenceOf
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

    fun selectRepairByUUID(uuid: String): Repair {
        return database.repairs.filter { it.uuid eq uuid }.first().value
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
