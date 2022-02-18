package com.gargantua7.cams.gp.server.model.vo

import com.gargantua7.cams.gp.server.model.dto.FullPerson

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
) {
    constructor(origin: FullPerson, privacy: Boolean = false) : this(
        origin.username,
        origin.name,
        origin.major,
        origin.collage,
        origin.majorId,
        origin.dep,
        origin.depId,
        if (privacy) null else origin.permission,
        origin.title,
        if (privacy) null else origin.phone,
        if (privacy) null else origin.wechat
    )
}
