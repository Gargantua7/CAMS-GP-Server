package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.RepairDao
import com.gargantua7.cams.gp.server.dao.ReplyDao
import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.model.dto.Reply
import org.apache.shiro.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Gargantua7
 */
@Service
@Transactional
class ReplyService {

    @Autowired
    private lateinit var repairService: RepairService

    @Autowired
    private lateinit var repairDao: RepairDao

    @Autowired
    private lateinit var replyDao: ReplyDao

    fun insertNewReply(reply: Reply) {
        val repair = repairDao.selectRepairByUUID(reply.repair)
        if (!repairService.permissionCheck(repair)) {
            throw AuthorizedException.InsufficientPermissionsException()
        }
        replyDao.insertNewReply(reply)
    }
}
