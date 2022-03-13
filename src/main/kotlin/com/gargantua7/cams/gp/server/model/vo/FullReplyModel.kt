package com.gargantua7.cams.gp.server.model.vo

import com.gargantua7.cams.gp.server.model.dto.FullReply
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
data class FullReplyModel(
    val id: Long,
    val repairId: Long,
    val repairTitle: String,
    val sender: FullPersonModel,
    val type: Int,
    val content: String,
    val time: LocalDateTime
) {
    constructor(origin: FullReply, privacy: Boolean = false) : this(
        origin.id,
        origin.repairId,
        origin.repairTitle,
        origin.sender.toVo(privacy),
        origin.type,
        origin.content,
        origin.time
    )
}
