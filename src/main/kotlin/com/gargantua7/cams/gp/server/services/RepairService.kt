package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.RepairDao
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
    private lateinit var repairDao: RepairDao

    fun insertNewRepair(repair: Repair) {
        repairDao.insert(repair)
    }
}
