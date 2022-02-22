package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.PersonDao
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
    private lateinit var repairDao: RepairDao

    @Autowired
    private lateinit var replyDao: ReplyDao

    fun insertNewReply(reply: Reply) {
        val repair = repairDao.selectRepairByUUID(reply.repair)
        if (repair.private && (reply.sender !in arrayOf(repair.initiator, repair.principal))) {
            val subject = SecurityUtils.getSubject()
            if (!(subject.hasRole("dep:1") && subject.isPermitted("Dep"))) {
                throw AuthorizedException("Insufficient Permissions")
            }
        }
        replyDao.insertNewReply(reply)
    }
}
