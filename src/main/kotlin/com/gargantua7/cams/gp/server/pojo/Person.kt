package com.gargantua7.cams.gp.server.pojo

import com.gargantua7.cams.gp.server.entity.PersonEntity

/**
 * @author Gargantua7
 */
data class Person(
    val username: String,
    var name: String,
    var majorId: String,
    var depId: Int,
    var permissionLevel: Int,
    var phone: String?,
    var wechat: String?
) {
    constructor(that: PersonEntity) : this(that.username, that.name, that.majorId, that.depId, that.permissionLevel, that.phone, that.wechat)

    fun getEntity() = PersonEntity {
        username = this@Person.username
        name = this@Person.name
        majorId = this@Person.majorId
        depId = this@Person.depId
        permissionLevel = this@Person.permissionLevel
        phone = this@Person.phone
        wechat = this@Person.wechat
    }
}
