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

    fun selectReplyUUIDListByRepairUUID(repairUUID: String): List<String> {
        if (!repairService.permissionCheck(repairUUID)) {
            throw AuthorizedException.InsufficientPermissionsException()
        }
        return replyDao.selectReplyUUIDListByRepairUUID(repairUUID)
    }

    fun selectReplyByUUID(uuid: String): Reply {
        val reply = replyDao.selectReplyByUUID(uuid)
        if (!repairService.permissionCheck(reply.repair)) {
            throw AuthorizedException.InsufficientPermissionsException()
        }
        return reply
    }
}
