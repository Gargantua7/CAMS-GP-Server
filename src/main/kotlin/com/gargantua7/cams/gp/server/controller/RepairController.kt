package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.model.dto.Repair
import com.gargantua7.cams.gp.server.model.vo.NewRepairModel
import com.gargantua7.cams.gp.server.services.RepairService
import org.apache.shiro.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Gargantua7
 */
@RestController
@RequestMapping(produces = ["application/json;charset=UTF-8"])
class RepairController {

    @Autowired
    private lateinit var repairService: RepairService

    @PostMapping("/private/repair/create")
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

}
