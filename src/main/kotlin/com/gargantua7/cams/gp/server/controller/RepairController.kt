package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.exception.BadRequestException
import com.gargantua7.cams.gp.server.model.dto.Repair
import com.gargantua7.cams.gp.server.model.dto.Reply
import com.gargantua7.cams.gp.server.model.vo.NewRepairModel
import com.gargantua7.cams.gp.server.model.vo.NewRepairReplyModel
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
        repairService.assignPrincipleByID(id, principle)
    }

    @RequiresAuthentication
    @PostMapping("/repair/{id}/state/change/{state}")
    fun changeState(@PathVariable id: Long, @PathVariable state: String) {
        if (state !in arrayOf("open", "close")) throw BadRequestException("Wrong Request Parma")
        repairService.changeStateByIDWithAuth(id, state == "open")
    }

    @GetMapping("/repair/id/list")
    fun getAll(): List<Long> {
        return repairService.selectAllRepairIDList()
    }

    @GetMapping("/repair/get/id/{id}")
    fun getByRepairID(@PathVariable id: Long): Repair {
        val repair = repairService.selectRepairByID(id)
        if (!repair.private) return repair
        val requestId =
            (SecurityUtils.getSubject().principal as String?)
                ?: throw AuthorizedException.InsufficientPermissionsException()
        if (requestId == repair.initiator || requestId == repair.principal) return repair
        val requester = personService.selectPersonByUsername(requestId)
        if (requester.permissionLevel > 3 && requester.depId == 1) return repair
        throw AuthorizedException.InsufficientPermissionsException()
    }


    @GetMapping("/repair/id/list/person/{username}")
    fun getByPersonUsername(@PathVariable username: String): List<Long> {
        return repairService.selectRepairIDListByPerson(username)
    }

    @RequiresAuthentication
    @RequiresRoles("dep:1")
    @RequiresPermissions("Dep")
    @GetMapping("/repair/id/list/unassigned")
    fun getUnassigned(): List<Long> {
        return repairService.selectRepairIDWithUnassigned()
    }

    @GetMapping("/repair/id/list/keyword/{key}")
    fun search(@PathVariable key: String): List<Long> {
        return repairService.selectRepairIDListByKeyword(key)
    }


    // -------- Repair Reply -------------

    @RequiresAuthentication
    @PostMapping("/repair/{id}/reply/add")
    fun addReply(@PathVariable id: Long, @RequestBody model: NewRepairReplyModel) {
        if (model.content.isBlank())
            throw BadRequestException.RequestParamFormatException("Content Must Not Empty Or Blank")
        val reply = Reply(
            repair = id,
            sender = SecurityUtils.getSubject().principal as String,
            content = model.content
        )
        replyService.insertNewReply(reply)
    }

    @GetMapping("/repair/{id}/reply/list")
    fun getReplyListWithRepairID(@PathVariable id: Long): List<Long> {
        return replyService.selectReplyIDListByRepairUUID(id)
    }

    @GetMapping("/repair/reply/get/{id}")
    fun getReplyByID(@PathVariable id: Long): Reply {
        return replyService.selectReplyByID(id)
    }
}
