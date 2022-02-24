package com.gargantua7.cams.gp.server.model.dto

import com.gargantua7.cams.gp.server.model.po.PersonEntity

/**
 * @author Gargantua7
 */
data class Person(
    val username: String,
    val name: String,
    val sex: Boolean,
    val majorId: String,
    val depId: Int,
    val permissionLevel: Int,
    val phone: String?,
    val wechat: String?
) {
    constructor(that: PersonEntity) : this(
        that.username,
        that.name,
        that.sex,
        that.majorId,
        that.depId,
        that.permissionLevel,
        that.phone,
        that.wechat
    )

    val entity
        get() = PersonEntity {
            username = this@Person.username
            name = this@Person.name
            sex = this@Person.sex
            majorId = this@Person.majorId
            depId = this@Person.depId
            permissionLevel = this@Person.permissionLevel
            phone = this@Person.phone
            wechat = this@Person.wechat
        }
}
