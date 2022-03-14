package com.gargantua7.cams.gp.server.controller

import com.gargantua7.cams.gp.server.model.dto.Message
import com.gargantua7.cams.gp.server.model.vo.NewMsgModel
import com.gargantua7.cams.gp.server.services.MsgService
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.annotation.RequiresAuthentication
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
class MsgController {

    @Autowired
    private lateinit var msgService: MsgService

    @RequiresAuthentication
    @PostMapping("/msg/send")
    fun send(@RequestBody msgModel: NewMsgModel) {
        val msg = Message.Normal(
            SecurityUtils.getSubject().principal as String,
            msgModel.recipient,
            msgModel.content
        )
        msgService.sendMsg(msg)
    }

    @RequiresAuthentication
    @PostMapping("/msg/get")
    fun get(): List<Message> {
        return msgService.getMsgList()
    }

}
