package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.FullReply
import com.gargantua7.cams.gp.server.model.dto.Reply
import com.gargantua7.cams.gp.server.model.dto.SearchPersonModel
import com.gargantua7.cams.gp.server.model.po.Repairs
import com.gargantua7.cams.gp.server.model.po.Replies
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * @author Gargantua7
 */
@Repository
class ReplyDao {

    @Autowired
    private lateinit var personDao: PersonDao

    @Autowired
    private lateinit var database: Database

    private val Database.replies get() = sequenceOf(Replies)

    fun insertNewReply(reply: Reply): Int {
        return database.replies.add(reply.entity)
    }

    fun selectReplyListByRepairID(repairID: Long, page: Int): List<FullReply> {
        return database
            .from(Replies)
            .leftJoin(Repairs, Replies.repair eq Repairs.id)
            .select()
            .where { Repairs.id eq repairID }
            .orderBy(Replies.time.desc())
            .limit(page * 10, 10)
            .map {
                FullReply(
                    it[Replies.id]!!,
                    it[Replies.repair]!!,
                    it[Repairs.title]!!,
                    personDao.selectPersonByConditional(SearchPersonModel(it[Replies.sender]!!), 0).single(),
                    it[Replies.type]!!,
                    it[Replies.content]!!,
                    it[Replies.time]!!
                )
            }
    }

    fun selectReplyByID(id: Long): FullReply {
        return database
            .from(Replies)
            .leftJoin(Repairs, Replies.repair eq Repairs.id)
            .select()
            .where { Replies.id eq id }
            .map {
                FullReply(
                    it[Replies.id]!!,
                    it[Replies.repair]!!,
                    it[Repairs.title]!!,
                    personDao.selectPersonByConditional(SearchPersonModel(it[Replies.sender]!!), 0).single(),
                    it[Replies.type]!!,
                    it[Replies.content]!!,
                    it[Replies.time]!!
                )
            }.single()
    }

}
