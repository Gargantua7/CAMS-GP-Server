package com.gargantua7.cams.gp.server.services

import com.gargantua7.cams.gp.server.model.dto.Message
import com.google.gson.Gson
import org.apache.shiro.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

/**
 * @author Gargantua7
 */
@Service
class MsgService {

    @Autowired
    private lateinit var gson: Gson

    @Autowired
    private lateinit var redisTemplate: StringRedisTemplate

    fun sendMsg(msg: Message) {
        redisTemplate.opsForList().rightPush(msg.recipient, gson.toJson(msg))
    }

    fun getMsgList(id: String = SecurityUtils.getSubject().principal as String): List<Message> {
        return buildList {
            while (redisTemplate.opsForList().size(id)!! > 0) {
                add(gson.fromJson(redisTemplate.opsForList().leftPop(id), Message::class.java))
            }
        }
    }
}
