package com.gargantua7.cams.gp.server.model.dto

import com.gargantua7.cams.gp.server.model.vo.FullRepairModel
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
data class FullRepair(
    val id: Long,
    val title: String,
    val content: String,
    val initiator: FullPerson,
    val principal: FullPerson?,
    val initTime: LocalDateTime,
    val updateTime: LocalDateTime,
    val state: Boolean = true,
    val private: Boolean = false,
    val reply: Int = 0
) {
    fun toVo(initiatorPrivacy: Boolean = false, principalPrivacy: Boolean = false) =
        FullRepairModel(this, initiatorPrivacy, principalPrivacy)
}
