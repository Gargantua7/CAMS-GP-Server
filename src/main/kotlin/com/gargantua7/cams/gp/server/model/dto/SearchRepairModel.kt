package com.gargantua7.cams.gp.server.model.dto

/**
 * @author Gargantua7
 */
data class SearchRepairModel(
    val id: Long?,
    val keyword: String?,
    val initiator: String?,
    val principal: String?,
    val state: Boolean?,
    val unassigned: Boolean = false,
)
