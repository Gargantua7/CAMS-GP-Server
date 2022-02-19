package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.exception.BadRequestException
import com.gargantua7.cams.gp.server.model.dto.Repair
import com.gargantua7.cams.gp.server.model.vo.NewRepairModel
import com.gargantua7.cams.gp.server.services.RepairService
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author Gargantua7
 */
@RestController
@RequestMapping(produces = ["application/json;charset=UTF-8"])
class RepairController {

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
}
