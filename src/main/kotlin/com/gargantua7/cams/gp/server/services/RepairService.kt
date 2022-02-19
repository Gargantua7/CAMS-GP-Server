package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.PersonDao
import com.gargantua7.cams.gp.server.dao.RepairDao
import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.exception.NotFoundException
import com.gargantua7.cams.gp.server.model.dto.Repair
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
        repairDao.assignPrincipleByUUID(uuid, principle)
    }

    fun changeStateByUUIDWithAuth(uuid: String, state: Boolean, requesterId: String) {
        val requester = personDao.selectPersonByUsername(requesterId)
        if (requester.depId != 1 || requester.permissionLevel < 3) {
            val repair = repairDao.selectRepairByUUID(uuid)
            if (requesterId != repair.initiator && requesterId != repair.principal)
                throw AuthorizedException("Insufficient Permissions")
        }
        if (repairDao.changeStateByUUID(uuid, state) != 1)
            throw NotFoundException("Repair[$uuid] Not Found")
    }
}
