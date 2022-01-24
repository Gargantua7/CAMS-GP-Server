package com.gargantua7.cams.gp.server.model.vo

/**
 * @author Gargantua7
 */
data class PersonInfo(
    val username: String,
    val name: String,
    val major: String,
    val collage: String,
    val dep: String,
    val permission: Int,
    val title: String,
    val phone: String?,
    val wechat: String?
)
