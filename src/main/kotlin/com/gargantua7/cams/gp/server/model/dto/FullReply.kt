package com.gargantua7.cams.gp.server.model.dto

import com.gargantua7.cams.gp.server.model.vo.FullReplyModel
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
data class FullReply(
    val id: Long,
    val repairId: Long,
    val repairTitle: String,
    val sender: FullPerson,
    val type: Int,
    val content: String,
    val time: LocalDateTime
) {
    fun toVo(privacy: Boolean = false) = FullReplyModel(this, privacy)
}
