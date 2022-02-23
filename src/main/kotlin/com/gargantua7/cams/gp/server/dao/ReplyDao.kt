package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Reply
import com.gargantua7.cams.gp.server.model.po.Replies
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * @author Gargantua7
 */
@Repository
class ReplyDao {

    @Autowired
    private lateinit var database: Database

    private val Database.replies get() = sequenceOf(Replies)

    fun insertNewReply(reply: Reply): Int {
        return database.replies.add(reply.entity)
    }

    fun selectReplyIDListByRepairID(repairID: Long): List<Long> {
        return database.replies.filter { it.repair eq repairID }.map { it.id }
    }

    fun selectReplyByID(id: Long): Reply {
        return database.replies.filter { it.id eq id }.single().value
    }

}
