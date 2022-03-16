package com.gargantua7.cams.gp.server.model.vo

import com.gargantua7.cams.gp.server.model.dto.FullRepair
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
data class FullRepairModel(
    val id: Long,
    val title: String,
    val content: String,
    val initiator: FullPersonModel,
    val principal: FullPersonModel?,
    val initTime: LocalDateTime,
    val updateTime: LocalDateTime,
    val state: Boolean = true,
    val private: Boolean = false,
    val reply: Int = 0
) {
    constructor(origin: FullRepair, initiatorPrivacy: Boolean = false, principalPrivacy: Boolean = false) : this(
        origin.id,
        origin.title,
        origin.content,
        origin.initiator.toVo(initiatorPrivacy),
        origin.principal?.toVo(principalPrivacy),
        origin.initTime,
        origin.updateTime,
        origin.state,
        origin.private,
        origin.reply
    )
}
