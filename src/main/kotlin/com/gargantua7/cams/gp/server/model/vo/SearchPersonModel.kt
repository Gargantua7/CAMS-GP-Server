package com.gargantua7.cams.gp.server.model.vo

/**
 * @author Gargantua7
 */
data class SearchPersonModel(
    val username: String?,
    val name: String?,
    val sex: Boolean?,
    val depId: Int?,
    val permissionLevel: Int?
)
