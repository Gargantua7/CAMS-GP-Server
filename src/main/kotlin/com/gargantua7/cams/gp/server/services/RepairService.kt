package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.dao.RepairDao
import com.gargantua7.cams.gp.server.dao.ReplyDao
import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.model.dto.FullRepair
import com.gargantua7.cams.gp.server.model.dto.Repair
import com.gargantua7.cams.gp.server.model.dto.Reply
import com.gargantua7.cams.gp.server.model.dto.SearchRepairModel
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
    private lateinit var replyDao: ReplyDao

    @Autowired
    private lateinit var repairDao: RepairDao

    fun insertNewRepair(repair: Repair) {
        repairDao.insert(repair)
    }

    fun assignPrincipleByID(id: Long, principle: String) {
        val repair = repairDao.selectRepairByID(id)
        if (repair.principal == principle) return
        repairDao.assignPrincipleByID(id, principle)
        replyDao.insertNewReply(
            Reply(
                repair = id,
                sender = SecurityUtils.getSubject().principal as String,
                type = 2,
                content = principle
            )
        )
    }

    fun selectAllRepairList(model: SearchRepairModel, page: Int): List<FullRepair> {
        return repairDao.selectRepairByConditional(model, page)
    }

    fun changeStateByIDWithAuth(id: Long, state: Boolean) {
        val repair = repairDao.selectRepairByID(id)
        if (!permissionCheck(repair)) {
            throw AuthorizedException.InsufficientPermissionsException()
        }
        if (repair.state == state) return
        repairDao.changeStateByID(id, state)
        replyDao.insertNewReply(
            Reply(
                repair = id,
                sender = SecurityUtils.getSubject().principal as String,
                type = 1,
                content = if (state) "Open" else "Close"
            )
        )
    }

    fun permissionCheck(id: Long): Boolean {
        val subject = SecurityUtils.getSubject()
        if (subject.hasRole("dep:1") && subject.isPermitted("Dep")) return true
        return permissionCheck(repairDao.selectRepairByID(id))
    }

    fun permissionCheck(repair: Repair): Boolean {
        val subject = SecurityUtils.getSubject()
        if (subject.hasRole("dep:1") && subject.isPermitted("Dep")) return true
        return !repair.private || ((subject.principal as String? ?: return false) in arrayOf(
            repair.initiator,
            repair.principal
        ))
    }
}
