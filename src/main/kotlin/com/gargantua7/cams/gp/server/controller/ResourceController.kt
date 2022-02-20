package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.dao.InfoDao
import com.gargantua7.cams.gp.server.model.dto.Collage
import com.gargantua7.cams.gp.server.model.dto.Department
import com.gargantua7.cams.gp.server.model.dto.Major
import com.gargantua7.cams.gp.server.model.dto.Permission
import com.gargantua7.cams.gp.server.util.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Gargantua7
 */
@RestController
@RequestMapping(produces = ["application/json;charset=UTF-8"])
class ResourceController {


    @Autowired
    private lateinit var infoDao: InfoDao

    @GetMapping("/res/list/dep")
    fun selectAllDep(): Any {
        return infoDao.selectAllDep().response
    }

    @GetMapping("/res/get/dep/{id}")
    fun selectDepById(@PathVariable id: Int): Department {
        return infoDao.selectDepById(id)
    }

    @GetMapping("/res/get/major/{id}")
    fun selectMajorById(@PathVariable id: String): Major {
        return infoDao.selectMajorById(id)
    }

    @GetMapping("/res/list/collage/{id}/major")
    fun selectMajorListByCollageId(@PathVariable id: String): Any {
        return infoDao.selectMajorListByCollageId(id).response
    }

    @GetMapping("/res/list/collage")
    fun selectAllCollage(): Any {
        return infoDao.selectAllCollage().response
    }

    @GetMapping("/res/get/collage/{id}")
    fun selectCollageById(@PathVariable id: String): Collage {
        return infoDao.selectCollageById(id)
    }

    @GetMapping("/res/get/permission/{level}")
    fun selectPermissionByLevel(@PathVariable level: Int): Permission {
        return infoDao.selectPermissionByLevel(level)
    }
}
