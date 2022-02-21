package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.PersonDao
import com.gargantua7.cams.gp.server.dao.RepairDao
import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.exception.NotFoundException
import com.gargantua7.cams.gp.server.model.dto.Repair
import org.apache.shiro.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Gargantua7
 */
@Service
@Transactional
class RepairService {

    @Autowired
    private lateinit var personDao: PersonDao

    @Autowired
    private lateinit var repairDao: RepairDao

    fun insertNewRepair(repair: Repair) {
        repairDao.insert(repair)
    }

    fun assignPrincipleByUUID(uuid: String, principle: String) {
        if (repairDao.assignPrincipleByUUID(uuid, principle) != 1)
            throw NotFoundException("Repair[$uuid] Not Found")
    }

    fun selectAllRepairUUIDListWithPage(page: Int): List<String> {
        return securityCalled(page, repairDao::selectAllRepairUUIDWithLimit)
    }

    fun selectRepairByUUID(uuid: String): Repair {
        try {
            return repairDao.selectRepairByUUID(uuid)
        } catch (e: NoSuchElementException) {
            throw NotFoundException("Repair[$uuid] Not Found", e)
        }
    }

    fun selectRepairUUIDListByKeyword(key: String): List<String> {
        return securityCalled(key, repairDao::selectRepairUUIDListByKeyword)
    }

    fun selectRepairUUIDWithUnassigned(): List<String> {
        return repairDao.selectRepairUUIDWithUnassigned()
    }

    fun changeStateByUUIDWithAuth(uuid: String, state: Boolean, requesterId: String) {
        val requester = personDao.selectPersonByUsername(requesterId)
        if (requester.depId != 1 || requester.permissionLevel < 3) {
            val repair = selectRepairByUUID(uuid)
            if (requesterId != repair.initiator && requesterId != repair.principal)
                throw AuthorizedException("Insufficient Permissions")
        }
        if (repairDao.changeStateByUUID(uuid, state) != 1)
            throw NotFoundException("Repair[$uuid] Not Found")
    }

    fun <P, R> securityCalled(param: P, action: (P, String) -> R): R {
        val subject = SecurityUtils.getSubject()
        return if (subject.hasRole("dep:1") && subject.isPermitted("Dep"))
            action(param, "")
        else action(param, (subject.principal as String?) ?: "-1")
    }
}
