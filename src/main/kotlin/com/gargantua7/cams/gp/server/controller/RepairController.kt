package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.exception.AuthorizedException
import com.gargantua7.cams.gp.server.exception.BadRequestException
import com.gargantua7.cams.gp.server.model.dto.Repair
import com.gargantua7.cams.gp.server.model.vo.NewRepairModel
import com.gargantua7.cams.gp.server.services.PersonService
import com.gargantua7.cams.gp.server.services.RepairService
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
    private lateinit var personService: PersonService

    @Autowired
    private lateinit var repairService: RepairService

    @RequiresAuthentication
    @PostMapping("/repair/create")
    fun createNewRepair(@RequestBody model: NewRepairModel) {
        model.require()
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
    @PostMapping("/repair/{uuid}/assign/{principle}")
    fun assignPrincipal(@PathVariable uuid: String, @PathVariable principle: String) {
        repairService.assignPrincipleByUUID(uuid, principle)
    }

    @RequiresAuthentication
    @PostMapping("/repair/{uuid}/state/change/{state}")
    fun changeState(@PathVariable uuid: String, @PathVariable state: String) {
        if (state !in arrayOf("open", "close")) throw BadRequestException("Wrong Request Parma")
        repairService.changeStateByUUIDWithAuth(uuid, state == "open", SecurityUtils.getSubject().principal as String)
    }

    @GetMapping("/repair/uuid/list/page/{page}")
    fun getWithPage(@PathVariable page: Int): List<String> {
        return repairService.selectAllRepairUUIDListWithPage(page)
    }

    @GetMapping("/repair/get/uuid/{uuid}")
    fun getByUUID(@PathVariable uuid: String): Repair {
        val repair = repairService.selectRepairByUUID(uuid)
        if (!repair.private) return repair
        val requestId =
            (SecurityUtils.getSubject().principal as String?) ?: throw AuthorizedException("Insufficient Permissions")
        if (requestId == repair.initiator || requestId == repair.principal) return repair
        val requester = personService.selectPersonByUsername(requestId)
        if (requester.permissionLevel > 3 && requester.depId == 1) return repair
        throw AuthorizedException("Insufficient Permissions")
    }

    @RequiresAuthentication
    @RequiresRoles("dep:1")
    @RequiresPermissions("Dep")
    @GetMapping("/repair/uuid/list/unassigned")
    fun getUnassigned(): List<String> {
        return repairService.selectRepairUUIDWithUnassigned()
    }

    @GetMapping("/repair/uuid/list/keyword/{key}")
    fun search(@PathVariable key: String): List<String> {
        return repairService.selectRepairUUIDListByKeyword(key)
    }
}
