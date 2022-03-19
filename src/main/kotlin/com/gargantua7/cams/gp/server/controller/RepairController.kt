package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.exception.BadRequestException
import com.gargantua7.cams.gp.server.model.dto.Message
import com.gargantua7.cams.gp.server.model.dto.Repair
import com.gargantua7.cams.gp.server.model.dto.Reply
import com.gargantua7.cams.gp.server.model.dto.SearchRepairModel
import com.gargantua7.cams.gp.server.model.vo.FullRepairModel
import com.gargantua7.cams.gp.server.model.vo.FullReplyModel
import com.gargantua7.cams.gp.server.model.vo.NewRepairModel
import com.gargantua7.cams.gp.server.model.vo.NewRepairReplyModel
import com.gargantua7.cams.gp.server.services.MsgService
import com.gargantua7.cams.gp.server.services.PersonService
import com.gargantua7.cams.gp.server.services.RepairService
import com.gargantua7.cams.gp.server.services.ReplyService
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author Gargantua7
 */
@RestController
@RequestMapping(produces = ["application/json;charset=UTF-8"])
class RepairController {

    @Autowired
    private lateinit var msgService: MsgService

    @Autowired
    private lateinit var replyService: ReplyService

    @Autowired
    private lateinit var personService: PersonService

    @Autowired
    private lateinit var repairService: RepairService

    @RequiresAuthentication
    @PostMapping("/repair/create")
    fun createNewRepair(@RequestBody model: NewRepairModel) {
        val repair = Repair(
            title = model.title,
            content = model.content,
            initiator = SecurityUtils.getSubject().principal as String,
            private = model.private
        )
        repairService.insertNewRepair(repair)
    }

    @RequiresAuthentication
    @RequiresRoles("dep:1")
    @RequiresPermissions("Dep")
    @PostMapping("/repair/{id}/assign/{principle}")
    fun assignPrincipal(@PathVariable id: Long, @PathVariable principle: String) {
        val su = repairService.assignPrincipleByID(id, principle)
        if (!su) return
        val repair = repairService.selectRepairById(id)
        msgService.sendMsg(Message.Repair(repair.initiator, id))
        msgService.sendMsg(Message.Repair(repair.principal!!, id))
    }

    @RequiresAuthentication
    @PostMapping("/repair/{id}/state/change/{state}")
    fun changeState(@PathVariable id: Long, @PathVariable state: String) {
        if (state !in arrayOf("open", "close")) throw BadRequestException("Wrong Request Parma")
        repairService.changeStateByIDWithAuth(id, state == "open")
    }

    @GetMapping("/repair/search/{page}")
    fun getAllByPage(model: SearchRepairModel, @PathVariable page: Int): List<FullRepairModel> {
        val requesterId = SecurityUtils.getSubject().principal as String?
        val requester = requesterId?.let { personService.selectPersonByUsername(it) }
        val requesterPermission = requester?.permissionLevel ?: -99
        return repairService.selectAllRepairList(model, page).map {
            it.toVo(
                requesterId != it.initiator.username && requesterPermission <= it.initiator.permission,
                requesterId != it.principal?.username && requesterPermission <= (it.principal?.permission ?: -99)
            )
        }
    }


    // -------- Repair Reply -------------

    @RequiresAuthentication
    @PostMapping("/repair/{id}/reply/add")
    fun addReply(@PathVariable id: Long, @RequestBody model: NewRepairReplyModel) {
        if (model.content.isBlank())
            throw BadRequestException.RequestParamFormatException("Content Must Not Empty Or Blank")
        val sender = SecurityUtils.getSubject().principal as String
        val reply = Reply(
            repair = id,
            sender = sender,
            content = model.content
        )
        replyService.insertNewReply(reply)
        val repair = repairService.selectRepairById(id)
        msgService.sendMsg(Message.Reply(sender, repair.initiator, id))
        repair.principal?.let { msgService.sendMsg(Message.Reply(sender, it, id)) }
    }

    @GetMapping("/repair/{id}/reply/list/{page}")
    fun getReplyListWithRepairID(@PathVariable id: Long, @PathVariable page: Int): List<FullReplyModel> {
        val requesterId = SecurityUtils.getSubject().principal as String?
        val requester = requesterId?.let { personService.selectPersonByUsername(it) }
        val requesterPermission = requester?.permissionLevel ?: -99
        return replyService.selectReplyListByRepairUUID(id, page).map {
            it.toVo(requesterId != it.sender.username && requesterPermission <= it.sender.permission)
        }
    }

    @GetMapping("/repair/reply/get/{id}")
    fun getReplyByID(@PathVariable id: Long): FullReplyModel {
        val requesterId = SecurityUtils.getSubject().principal as String?
        val requester = requesterId?.let { personService.selectPersonByUsername(it) }
        val requesterPermission = requester?.permissionLevel ?: -99
        return replyService.selectReplyByID(id)
            .let { it.toVo(requesterId != it.sender.username && requesterPermission <= it.sender.permission) }
    }
}
