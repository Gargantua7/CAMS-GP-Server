package com.gargantua7.cams.gp.server

import com.gargantua7.cams.gp.server.model.dto.Message
import com.gargantua7.cams.gp.server.services.MsgService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CamsGpServerApplicationTests {

    @Autowired
    private lateinit var msgService: MsgService

    @Test
    fun send() {
        msgService.sendMsg(
            Message.Normal(
                "10001",
                "201841512107",
                "Test"
            )
        )

    }

    @Test
    fun get() {
        msgService.getMsgList("201841512107").forEach(::println)
    }

}
