package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.ReplyDao
import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.model.dto.Reply
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
    private lateinit var replyDao: ReplyDao

    fun insertNewReply(reply: Reply) {
        if (!repairService.permissionCheck(reply.repair)) {
            throw AuthorizedException.InsufficientPermissionsException()
        }
        replyDao.insertNewReply(reply)
    }

    fun selectReplyIDListByRepairUUID(repairID: Long): List<Long> {
        if (!repairService.permissionCheck(repairID)) {
            throw AuthorizedException.InsufficientPermissionsException()
        }
        return replyDao.selectReplyIDListByRepairID(repairID)
    }

    fun selectReplyByID(id: Long): Reply {
        val reply = replyDao.selectReplyByID(id)
        if (!repairService.permissionCheck(reply.repair)) {
            throw AuthorizedException.InsufficientPermissionsException()
        }
        return reply
    }
}
