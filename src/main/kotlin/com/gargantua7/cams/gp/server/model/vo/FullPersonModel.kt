package com.gargantua7.cams.gp.server.model.vo

/**
 * @author Gargantua7
 */
data class FullPersonModel(
    val username: String,
    val name: String,
    val major: String,
    val collage: String,
    val majorId: String,
    val dep: String,
    val depId: Int,
    val permission: Int?,
    val title: String,
    val phone: String?,
    val wechat: String?
)
