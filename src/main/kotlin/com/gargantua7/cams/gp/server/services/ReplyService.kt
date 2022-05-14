package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.PersonDao
import com.gargantua7.cams.gp.server.dao.RepairDao
import com.gargantua7.cams.gp.server.dao.ReplyDao
import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.exception.NotFoundException
import com.gargantua7.cams.gp.server.model.dto.FullReply
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
    private lateinit var personDao: PersonDao

    @Autowired
    private lateinit var repairDao: RepairDao

    @Autowired
    private lateinit var repairService: RepairService

    @Autowired
    private lateinit var replyDao: ReplyDao

    fun insertNewReply(reply: Reply) {
        if (!repairService.permissionCheck(reply.repair)) {
            throw AuthorizedException.InsufficientPermissionsException()
        }
        replyDao.insertNewReply(reply)
        repairDao.refreshUpdateTime(reply.repair)
    }

    fun selectReplyListByRepairUUID(repairID: Long, page: Int): List<FullReply> {
        if (!repairService.permissionCheck(repairID)) {
            throw AuthorizedException.InsufficientPermissionsException()
        }
        return replyDao.selectReplyListByRepairID(repairID, page).map {
            FullReply(
                it.id,
                it.repair,
                repairService.selectRepairById(it.repair).title,
                personDao.selectFullPersonByUsername(it.sender),
                it.type,
                it.content,
                it.time
            )
        }
    }

    fun selectReplyByID(id: Long): FullReply {
        val reply = replyDao.selectReplyByID(id)
        if (!repairService.permissionCheck(reply.repair)) {
            throw AuthorizedException.InsufficientPermissionsException()
        }
        return FullReply(
            reply.id,
            reply.repair,
            repairService.selectRepairById(reply.repair).title,
            personDao.selectFullPersonByUsername(reply.sender),
            reply.type,
            reply.content,
            reply.time
        )
    }

    fun deleteReplyByID(id: Long) {
        val line = replyDao.deleteReplyByID(id)
        if (line == 0) throw NotFoundException("Reply[$id] not found")
    }
}
