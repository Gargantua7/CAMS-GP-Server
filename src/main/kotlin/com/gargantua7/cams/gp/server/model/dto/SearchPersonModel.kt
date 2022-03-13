package com.gargantua7.cams.gp.server.model.dto

/**
 * @author Gargantua7
 */
data class SearchPersonModel(
    val username: String? = null,
    val name: String? = null,
    val sex: Boolean? = null,
    val depId: Int? = null,
    val permissionLevel: Int? = null
)
